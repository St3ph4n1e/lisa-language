package org.example.vm;

import org.example.exceptions.RuntimeError;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class BytecodeSerializer {
    // Tags pour les types de constantes
    private static final byte TYPE_NUMBER = 0;
    private static final byte TYPE_BOOL   = 1;
    private static final byte TYPE_STRING = 2;

    // Magic number to know its a Lisa file
    private static final byte[] MAGIC = new byte[] { 'L', 'I', 'S', 'A' };

    /**
     * Save a chunk (bytecode + constantes) in a binary file.
     */
    public static void saveBytecode(Chunk chunk, String path) {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(path))) {
            // 1) Magic number
            out.write(MAGIC);
            // 2) Section constants
            List<Object> constants = chunk.getConstants();
            out.writeInt(constants.size());

            for (Object c : constants) {
                if (c instanceof Number number) {
                    out.writeByte(TYPE_NUMBER);
                    out.writeDouble(number.doubleValue());
                } else if (c instanceof Boolean bool) {
                    out.writeByte(TYPE_BOOL);
                    out.writeBoolean(bool);
                } else if (c instanceof String s) {
                    out.writeByte(TYPE_STRING);
                    byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
                    out.writeInt(bytes.length);
                    out.write(bytes);
                } else {
                    throw new IOException(
                            "Type de constante non supporté : " + c.getClass()
                    );
                }
            }

            // 3) Code
            List<Integer> code = chunk.getCode();
            out.writeInt(code.size());
            for (int value : code) {
                out.writeByte(value);
            }

        } catch (IOException e) {
            throw new RuntimeError(
                    "Impossible d'écrire le bytecode dans le fichier",
                    null,
                    e.getMessage()
            );
        }
    }

    /**
     * Charge un Chunk depuis un fichier binaire.
     */
    public static Chunk loadBytecode(String path) {
        try (DataInputStream in = new DataInputStream(new FileInputStream(path))) {

            // 1) Magic number
            byte[] magic = new byte[4];
            in.readFully(magic);
            if (!matchesMagic(magic)) {
                throw new RuntimeError(
                        "Fichier bytecode invalide (magic number incorrect)",
                        null,
                        new String(magic)
                );
            }

            Chunk chunk = new Chunk();

            // 2) Constantes
            int constCount = in.readInt();
            for (int i = 0; i < constCount; i++) {
                int tag = in.readUnsignedByte();

                switch (tag) {
                    case TYPE_NUMBER -> {
                        double value = in.readDouble();
                        chunk.addConstant(value);
                    }
                    case TYPE_BOOL -> {
                        boolean b = in.readBoolean();
                        chunk.addConstant(b);
                    }
                    case TYPE_STRING -> {
                        int len = in.readInt();
                        byte[] bytes = new byte[len];
                        in.readFully(bytes);
                        String s = new String(bytes, StandardCharsets.UTF_8);
                        chunk.addConstant(s);
                    }
                    default -> throw new RuntimeError(
                            "Tag de constante inconnu : " + tag,
                            null,
                            String.valueOf(tag)
                    );
                }
            }

            // 3) Code
            int codeLength = in.readInt();
            for (int i = 0; i < codeLength; i++) {
                int b = in.readUnsignedByte();
                chunk.getCode().add(b);
            }

            return chunk;

        } catch (IOException e) {
            throw new RuntimeError(
                    "Impossible de lire le bytecode depuis le fichier",
                    null,
                    e.getMessage()
            );
        }
    }

    //   Utils methods

    // Constantes : écriture
    // ============================================================

    private static void writeConstant(DataOutputStream out, Object value) throws IOException {
        if (value instanceof Number number) {
            out.writeByte(TYPE_NUMBER);          // tag
            out.writeDouble(number.doubleValue());
        } else if (value instanceof Boolean bool) {
            out.writeByte(TYPE_BOOL);            // tag
            out.writeBoolean(bool);
        } else if (value instanceof String s) {
            out.writeByte(TYPE_STRING);          // tag
            byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
            out.writeInt(bytes.length);          // longueur
            out.write(bytes);                    // contenu
        } else {
            throw new IOException("Type de constante non supporté : " + value.getClass());
        }
    }

    // Constantes : lecture

    private static Object readConstant(DataInputStream in) throws IOException {
        byte tag = in.readByte();
        return switch (tag) {
            case TYPE_NUMBER -> in.readDouble();
            case TYPE_BOOL   -> in.readBoolean();
            case TYPE_STRING -> {
                int len = in.readInt();
                byte[] bytes = new byte[len];
                in.readFully(bytes);
                yield new String(bytes, StandardCharsets.UTF_8);
            }
            default -> throw new IOException("Tag de constante inconnu : " + tag);
        };
    }


    private static boolean matchesMagic(byte[] magic) {
        if (magic.length != MAGIC.length) return false;
        for (int i = 0; i < MAGIC.length; i++) {
            if (magic[i] != MAGIC[i]) return false;
        }
        return true;
    }
}
