#!/usr/bin/env bash
cd "$(dirname "$0")"
REPO_DIR=~/.capstan/repository/mayankrastogi/osv-openjdk8
echo "Creating directories..."
mkdir -p ${REPO_DIR}
echo "Extracting base images to ${REPO_DIR}..."
shopt -s nullglob
for f in *.gz; do
  IMG=$(basename "${f}" .gz)
  gunzip -c "${f}" > ${REPO_DIR}/"${IMG}"
done
echo "Done."
