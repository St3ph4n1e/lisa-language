# Prompts utilisés pour le développement du Langage Lisa

Ce document regroupe les prompts envoyés à l’IA dans le cadre du développement du langage **Lisa**.

---

##  1. **Réalisation du Pratt parser**

[Prompts Clés]

### • Explication du Pratt Parser

**Prompt** :

> Explique moi ce que représente le Pratt Parser dans un langage de programmation ainsi que son importance vis a vis
> des autres elements d'un projet de langage de programmation "

### • Comparaison Parser Python/JAVA

**Prompt** :

> " Voici ci-joint un Parser d'un Projet de langage de programmation Python (Celui de Toy), a quoi pourrait ressembler son equivalent en java ? "

[Refacto]

### • Analyse d’un Parser implémenté

**Prompt** :

> Voici mon code de parser (Pratt Parser quasiment final) , analyse-le et donne-moi les problèmes, incohérences et améliorations possibles.

##  2. ** Modulation du langage **

[Debug]

### • Erreur REPL avec Boucles

**Prompt** :
> " Lorsque j'utilise ce type d'instruction dans mon REPL :
> ```lisa
> variable a est 0;
> tant_que (a plus_petit_que 5) debut
>     affiche a;
>     a devient a + 1;
> fin
> ```
>
> J'obtiens une erreur au lieu d'afficher un compte de 0 à 4 comme prévu. Ce que j'ai mis en place n'assure pas la modularité du langage . Aide-moi à débloquer cette situation. "

**Réponse de l'IA** :
> « Deux problèmes détectés :
>
> 1. **Bug `devient`** : Le parser n'accepte que `TokenType.EQUAL` dans `parseAssignment()`. Ajoute :
     >    ```java
>    if(match(TokenType.EQUAL) || match(TokenType.ASSIGN)){

>
> 2. **Fonctions retournent `false`** : Dans `LisaFunction.call()`, tu retournes toujours `false`. Il faut capturer `ReturnException` pour récupérer la vraie valeur de retour. »

[Halucinations]

Claude.ai a improvisé des imports non présents dans le code alors qu’il existait déjà des classes dans ast qui remplissaient ce rôle.
Je les ai donc remplacés par les classes qui correspondait à mon cas.


##  3. **Réalisation des tests des fonctions dans le REPL**

[Prompts Clés]

### • Conversion d'une instruction en test unitaire

**Prompt** :
> J'utilise ce type de ligne dans mon REPL pour tester si le langage Lisa supporte les fonctions et la récursion 
> `variable age est 17; si (age plus_grand_que  17) debut affiche "Vous êtes majeur !"; fin sinon debut affiche "Vous etes mineur !"; fin` 
> 
> Montre moi comment convertir cette ligne en fonction de test unitaire avec les meme prérequis que InterpreterTest

