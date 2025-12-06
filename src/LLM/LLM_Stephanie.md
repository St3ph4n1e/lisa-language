# Prompts utilisés pour le développement du Lexer (Langage Lisa)

Ce document regroupe les prompts envoyés à l’IA dans le cadre du développement du lexer du langage **Lisa**.

---

##  1. **Construction du Lexer**

### **Extraction des lexèmes**
> « Comment je peux traduire ça en Java ?  
> `text = self.source[self.start : self.current]` »

### **Détection alphanumérique**
> « Il n'y a pas isAlphanum en Java ? »

### **Gestion du literal dans `addToken`**
> « Dans ma méthode addToken je dois initialiser à null le literal car je dois le garder mais je ne sais pas comment faire, tu peux m'aider ? »

### **Structure des messages d’erreur lexicale**
> « Est-ce que le fait que l’erreur s’affiche sur deux lignes est utile ?  
> 'Erreur lexicale : caractère inattendu @'  
> 'Ligne : 4' »

---

## 2. **Tests du Lexer**

### **Compréhension d’un test qui échoue**
> « Le test fail toujours. Expected […] Actual […]. Tu peux m’aider à voir pourquoi ? »

---

## 3. **Mots-clés et Tokens du langage Lisa**

### **Correction d’un appel incorrect à KEYWORDS**
> « Il faut que je corrige cette ligne :  
> `TokenType tokenType = Keywords.KEYWORDS(text, TokenType.IDENTIFIER);` »

---

##  4. **Gestion des erreurs**

### **Compréhension de la story "Gestion d’erreurs"**
> « Tu peux m’expliquer la gestion des erreurs (panic mode, Result type, runtime error, etc.) ? »

### **Erreur sur mot-clé mal orthographié**
> « Dans le cas où ce serait un mot-clé mal écrit, on crée une nouvelle erreur ? Explique-moi juste ne génère rien. »
---

## 5. Mise en place de la VM + Bytecode (Story #14)

### . Compréhension du fonctionnement de la VM
> « C’est quoi exactement un jump ? Pourquoi on lit deux bytes pour un jump offset ? »

### . Reverse Polish Notation (évaluation postfixée)
> « Pourquoi la VM pousse d’abord les deux opérandes avant d’exécuter ADD ? »

### . Conversion float/double dans la VM
> « Je veux éviter les ClassCastException dans ma VM. Comment gérer proprement Float vs Double ? »

---

## 6. Compilation vers bytecode

### • Compilation des expressions
> « On peut réécrire compileExpression avec switch-case plutôt que instance-of ? »

### • Support des blocs
> « Comment compiler un BlockStatement dans mon bytecode ? »

### • Gestion des variables globales dans la VM
> « Comment implanter GET_GLOBAL et SET_GLOBAL ? Je mets une table dans la VM ou ailleurs ? »

---

## 7. REPL amélioré avec coloration syntaxique

### • Implémentation de la coloration
> « Pourquoi mon keyword ‘plus_grand_que’ n’est pas coloré ? »

### • Mise en forme avec sauts de ligne
> « Pour le RBRACE le saut de ligne doit se trouver avant. C’est possible ? »
---

## 8. Corrections et améliorations diverses

### • Crash Java dans la VM
> « Il ne faut pas que mon programme crashe avec une erreur Java. Je veux une RuntimeError Lisa. Comment faire ? »
---

