grammar Lisa;

// Parser Rules
programme
    : instruction* EOF
    ;

instruction
    : declaration
    | affectation
    | affichage
    | conditionnel
    | boucle
    | fonction_declaration
    | retour
    | appel_fonction
    | bloc
    ;

bloc
    : DEBUT instruction* FIN
    ;

declaration
    : VARIABLE IDENTIFIANT EST expression POINT_VIRGULE
    ;

affectation
    : IDENTIFIANT EST expression POINT_VIRGULE
    ;

affichage
    : AFFICHE expression POINT_VIRGULE
    ;

conditionnel
    : SI PAREN_OUVRANTE condition PAREN_FERMANTE bloc (SINON bloc)?
    ;

boucle
    : TANT_QUE PAREN_OUVRANTE condition PAREN_FERMANTE bloc
    ;

fonction_declaration
    : FONCTION IDENTIFIANT PAREN_OUVRANTE parametres? PAREN_FERMANTE bloc
    ;

parametres
    : IDENTIFIANT (ET IDENTIFIANT)*
    ;

arguments
    : expression (ET expression)*
    ;

appel_fonction
    : IDENTIFIANT EST IDENTIFIANT PAREN_OUVRANTE arguments? PAREN_FERMANTE POINT_VIRGULE?
    ;

retour
    : RETOURNE expression POINT_VIRGULE
    ;

condition
    : expression comparateur expression
    ;

comparateur
    : EGALE
    | PLUS_PETIT_QUE
    | PLUS_GRAND_QUE
    ;

expression
    : expression operateur expression
    | IDENTIFIANT PAREN_OUVRANTE arguments? PAREN_FERMANTE  // function call in expression
    | IDENTIFIANT
    | NOMBRE
    | PAREN_OUVRANTE expression PAREN_FERMANTE
    ;

operateur
    : PLUS
    | MOINS
    | FOIS
    | DIVISE
    ;

// Lexer Rules (Tokens)
VARIABLE        : 'variable' ;
EST             : 'est' ;
AFFICHE         : 'affiche' ;
SI              : 'si' ;
SINON           : 'sinon' ;
TANT_QUE        : 'tant_que' ;
FONCTION        : 'fonction' ;
RETOURNE        : 'retourne' ;
DEBUT           : 'debut' ;
FIN             : 'fin' ;
ET              : 'et' ;

EGALE           : 'egale' ;
PLUS_PETIT_QUE  : 'plus_petit_que' ;
PLUS_GRAND_QUE  : 'plus_grand_que' ;

PLUS            : '+' ;
MOINS           : '-' ;
FOIS            : '*' ;
DIVISE          : '/' ;

PAREN_OUVRANTE  : '(' ;
PAREN_FERMANTE  : ')' ;
POINT_VIRGULE   : ';' ;

NOMBRE          : [0-9]+ ('.' [0-9]+)? ;
IDENTIFIANT     : [a-zA-Z_][a-zA-Z_0-9]* ;

// Skip whitespace and comments
WS              : [ \t\r\n]+ -> skip ;
COMMENTAIRE     : '//' ~[\r\n]* -> skip ;
