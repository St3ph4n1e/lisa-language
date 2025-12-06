package org.example.model.ast;

import org.example.model.Token;

import java.util.Objects;
import java.util.Optional;

public class ReturnStatement extends Statement {
    private Token keyword;
    private Optional<Expression> value;

    public ReturnStatement(Token keyword, Optional<Expression> value) {
        this.keyword = keyword;
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ReturnStatement that = (ReturnStatement) object;
        return Objects.equals(keyword, that.keyword) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyword, value);
    }

    @Override
    public String toString() {
        return "ReturnStatement{" +
                "keyword=" + keyword +
                ", value=" + value +
                '}';
    }

    public Optional<Expression> getValue() {
        return value;
    }
}
