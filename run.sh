#!/bin/bash

# En cas d'erreur, le script s'arrête immédiatement
set -e

# --- COULEURS ---
NC='\033[0m' 
ROUGE='\033[0;31m'
VERT='\033[0;32m'
JAUNE='\033[0;33m'
BLEU='\033[0;34m'
CYAN='\033[0;36m'

# --- CONFIGURATION ---
JAR_SOURCE="jar/volta-mvc.jar"
TARGET_DIR="../Test-Volta/lib"


# 1. Exécution du build du Framework
if [ -f "./build-jar.sh" ]; then
    echo -e "${BLEU}Lancement du script de build du JAR...${NC}\n"
    ./build-jar.sh
    echo ""
else
    echo -e "${ROUGE}Erreur : Le fichier ./build-jar.sh n'a pas été trouvé ici.${NC}"
    exit 1
fi

# 2. Vérification de la création du JAR obtenu
if [ ! -f "$JAR_SOURCE" ]; then
    echo -e "${ROUGE}Erreur : Le fichier $JAR_SOURCE n'a pas été généré par le build !${NC}"
    exit 1
fi

# 3. Vérification et création du dossier de destination s'il n'existe pas
if [ ! -d "$TARGET_DIR" ]; then
    echo -e "${JAUNE}Le dossier de destination $TARGET_DIR n'existe pas. Création...${NC}"
    mkdir -p "$TARGET_DIR"
fi

# 4. Copie et remplacement du JAR dans Test-Volta
echo -e "${BLEU}Copie de l'archive vers le projet de test...${NC}"
cp -f "$JAR_SOURCE" "$TARGET_DIR/"

echo -e "${VERT}Remplacement réussi !${NC} -> ${CYAN}$TARGET_DIR/$(basename "$JAR_SOURCE")${NC}"

echo -e "\n${CYAN}==================================================${NC}"
echo -e "${VERT}     PROCESSUS TERMINE AVEC SUCCES !${NC}"
echo -e "${CYAN}==================================================${NC}"