#!/bin/bash

set -e

# --- COULEURS ---
NC='\033[0m' 
ROUGE='\033[0;31m'
VERT='\033[0;32m'
JAUNE='\033[0;33m'
BLEU='\033[0;34m'
CYAN='\033[0;36m'

# --- CONFIGURATION ---
JAR_DIR="jar"
JAR_NAME="volta-mvc.jar"
BIN_DIR="bin"
LIB_DIR="lib"
SRC_DIR="src/main/java"

echo -e "${CYAN}Début du build de l'archive JAR...${NC}"

# 1. Nettoyage et création des répertoires
if [ -d "$BIN_DIR" ]; then
    echo -e "${JAUNE}Nettoyage de l'ancien dossier de compilation ($BIN_DIR)...${NC}"
    rm -rf "$BIN_DIR"
fi
mkdir "$BIN_DIR"

if [ ! -d "$JAR_DIR" ]; then
    echo -e "${JAUNE}Création du dossier de sortie '$JAR_DIR'...${NC}"
    mkdir "$JAR_DIR"
fi

# 2. Gestion des dépendances
echo -e "${BLEU}Enregistrement des bibliothèques dans lib/...${NC}"
CLASSPATH=$(find "$LIB_DIR" -name "*.jar" | tr '\n' ':')

# 3. Scan des sources
echo -e "${BLEU}Recherche de tous les fichiers sources .java...${NC}"
SOURCES_LIST=$(find "$SRC_DIR" -name "*.java")

if [ -z "$SOURCES_LIST" ]; then
    echo -e "${ROUGE}Erreur : Aucun fichier .java trouvé dans $SRC_DIR !${NC}"
    exit 1
fi

# 4. Compilation
echo -e "${CYAN}Compilation de tous les fichiers Java trouvés...${NC}"
javac -cp "$CLASSPATH" -d "$BIN_DIR" $SOURCES_LIST

# 5. Packaging JAR
echo -e "${CYAN}Empaquetage de toutes les classes dans le fichier JAR...${NC}"
cd "$BIN_DIR"
jar -cvf "../$JAR_DIR/$JAR_NAME" . > /dev/null
cd ..

# 6. Nettoyage final du dossier binaire
rm -rf "$BIN_DIR"

echo -e "${VERT}Succès ! L'archive $JAR_NAME a été générée dans /$JAR_DIR/.${NC}"