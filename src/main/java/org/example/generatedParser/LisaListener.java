// Generated from Lisa.g4 by ANTLR 4.13.2
package org.example.generatedParser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LisaParser}.
 */
public interface LisaListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LisaParser#programme}.
	 * @param ctx the parse tree
	 */
	void enterProgramme(LisaParser.ProgrammeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LisaParser#programme}.
	 * @param ctx the parse tree
	 */
	void exitProgramme(LisaParser.ProgrammeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LisaParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterInstruction(LisaParser.InstructionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LisaParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitInstruction(LisaParser.InstructionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LisaParser#bloc}.
	 * @param ctx the parse tree
	 */
	void enterBloc(LisaParser.BlocContext ctx);
	/**
	 * Exit a parse tree produced by {@link LisaParser#bloc}.
	 * @param ctx the parse tree
	 */
	void exitBloc(LisaParser.BlocContext ctx);
	/**
	 * Enter a parse tree produced by {@link LisaParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(LisaParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link LisaParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(LisaParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link LisaParser#affectation}.
	 * @param ctx the parse tree
	 */
	void enterAffectation(LisaParser.AffectationContext ctx);
	/**
	 * Exit a parse tree produced by {@link LisaParser#affectation}.
	 * @param ctx the parse tree
	 */
	void exitAffectation(LisaParser.AffectationContext ctx);
	/**
	 * Enter a parse tree produced by {@link LisaParser#affichage}.
	 * @param ctx the parse tree
	 */
	void enterAffichage(LisaParser.AffichageContext ctx);
	/**
	 * Exit a parse tree produced by {@link LisaParser#affichage}.
	 * @param ctx the parse tree
	 */
	void exitAffichage(LisaParser.AffichageContext ctx);
	/**
	 * Enter a parse tree produced by {@link LisaParser#conditionnel}.
	 * @param ctx the parse tree
	 */
	void enterConditionnel(LisaParser.ConditionnelContext ctx);
	/**
	 * Exit a parse tree produced by {@link LisaParser#conditionnel}.
	 * @param ctx the parse tree
	 */
	void exitConditionnel(LisaParser.ConditionnelContext ctx);
	/**
	 * Enter a parse tree produced by {@link LisaParser#boucle}.
	 * @param ctx the parse tree
	 */
	void enterBoucle(LisaParser.BoucleContext ctx);
	/**
	 * Exit a parse tree produced by {@link LisaParser#boucle}.
	 * @param ctx the parse tree
	 */
	void exitBoucle(LisaParser.BoucleContext ctx);
	/**
	 * Enter a parse tree produced by {@link LisaParser#fonction_declaration}.
	 * @param ctx the parse tree
	 */
	void enterFonction_declaration(LisaParser.Fonction_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link LisaParser#fonction_declaration}.
	 * @param ctx the parse tree
	 */
	void exitFonction_declaration(LisaParser.Fonction_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link LisaParser#parametres}.
	 * @param ctx the parse tree
	 */
	void enterParametres(LisaParser.ParametresContext ctx);
	/**
	 * Exit a parse tree produced by {@link LisaParser#parametres}.
	 * @param ctx the parse tree
	 */
	void exitParametres(LisaParser.ParametresContext ctx);
	/**
	 * Enter a parse tree produced by {@link LisaParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(LisaParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LisaParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(LisaParser.ArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LisaParser#appel_fonction}.
	 * @param ctx the parse tree
	 */
	void enterAppel_fonction(LisaParser.Appel_fonctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LisaParser#appel_fonction}.
	 * @param ctx the parse tree
	 */
	void exitAppel_fonction(LisaParser.Appel_fonctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LisaParser#retour}.
	 * @param ctx the parse tree
	 */
	void enterRetour(LisaParser.RetourContext ctx);
	/**
	 * Exit a parse tree produced by {@link LisaParser#retour}.
	 * @param ctx the parse tree
	 */
	void exitRetour(LisaParser.RetourContext ctx);
	/**
	 * Enter a parse tree produced by {@link LisaParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(LisaParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LisaParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(LisaParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LisaParser#comparateur}.
	 * @param ctx the parse tree
	 */
	void enterComparateur(LisaParser.ComparateurContext ctx);
	/**
	 * Exit a parse tree produced by {@link LisaParser#comparateur}.
	 * @param ctx the parse tree
	 */
	void exitComparateur(LisaParser.ComparateurContext ctx);
	/**
	 * Enter a parse tree produced by {@link LisaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(LisaParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LisaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(LisaParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LisaParser#operateur}.
	 * @param ctx the parse tree
	 */
	void enterOperateur(LisaParser.OperateurContext ctx);
	/**
	 * Exit a parse tree produced by {@link LisaParser#operateur}.
	 * @param ctx the parse tree
	 */
	void exitOperateur(LisaParser.OperateurContext ctx);
}