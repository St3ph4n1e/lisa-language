// Generated from Lisa.g4 by ANTLR 4.13.2
package org.example.generatedParser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LisaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LisaVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LisaParser#programme}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgramme(LisaParser.ProgrammeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LisaParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstruction(LisaParser.InstructionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LisaParser#bloc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBloc(LisaParser.BlocContext ctx);
	/**
	 * Visit a parse tree produced by {@link LisaParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(LisaParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link LisaParser#affectation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAffectation(LisaParser.AffectationContext ctx);
	/**
	 * Visit a parse tree produced by {@link LisaParser#affichage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAffichage(LisaParser.AffichageContext ctx);
	/**
	 * Visit a parse tree produced by {@link LisaParser#conditionnel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionnel(LisaParser.ConditionnelContext ctx);
	/**
	 * Visit a parse tree produced by {@link LisaParser#boucle}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoucle(LisaParser.BoucleContext ctx);
	/**
	 * Visit a parse tree produced by {@link LisaParser#fonction_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFonction_declaration(LisaParser.Fonction_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link LisaParser#parametres}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParametres(LisaParser.ParametresContext ctx);
	/**
	 * Visit a parse tree produced by {@link LisaParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(LisaParser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LisaParser#appel_fonction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAppel_fonction(LisaParser.Appel_fonctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LisaParser#retour}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRetour(LisaParser.RetourContext ctx);
	/**
	 * Visit a parse tree produced by {@link LisaParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(LisaParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LisaParser#comparateur}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparateur(LisaParser.ComparateurContext ctx);
	/**
	 * Visit a parse tree produced by {@link LisaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(LisaParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LisaParser#operateur}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperateur(LisaParser.OperateurContext ctx);
}