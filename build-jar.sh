#!/bin/bash

set -e

JAR_DIR="jar"
JAR_NAME="volta-mvc.jar"
BIN_DIR="bin"
LIB_DIR="lib"
SRC_DIR="src/main/java"

echo " Début du build  de l'archive JAR..."

if [ -d "$BIN_DIR" ]; then
    echo "Nettoyage de l'ancien dossier de compilation ($BIN_DIR)..."
    rm -rf "$BIN_DIR"
fi
mkdir "$BIN_DIR"

if [ ! -d "$JAR_DIR" ]; then
    echo "Création du dossier de sortie '$JAR_DIR'..."
    mkdir "$JAR_DIR"
fi


echo "Enregistrement des bibliothèques dans lib/..."
CLASSPATH=$(find "$LIB_DIR" -name "*.jar" | tr '\n' ':')

echo "Recherche de tous les fichiers sources .java..."
SOURCES_LIST=$(find "$SRC_DIR" -name "*.java")

if [ -z "$SOURCES_LIST" ]; then
    echo "❌ Erreur : Aucun fichier .java trouvé dans $SRC_DIR !"
    exit 1
fi

echo "Compilation de tous les fichiers Java trouvés..."
javac -cp "$CLASSPATH" -d "$BIN_DIR" $SOURCES_LIST

echo "Empaquetage de toutes les classes dans le fichier JAR..."
cd "$BIN_DIR"
jar -cvf "../$JAR_DIR/$JAR_NAME" . > /dev/null
cd ..

rm -rf "$BIN_DIR"

echo "Succès ! L'archive  $JAR_NAME a été générée dans /$JAR_DIR/."