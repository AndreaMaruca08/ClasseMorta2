#!/bin/sh

# Stampare in log la verifica della connessione
echo "Attendo PostgreSQL su $1:$2..."

# Attendere finché PostgreSQL non è raggiungibile
while ! nc -z $1 $2; do
  sleep 1
done

echo "PostgreSQL è pronto!"

# Esegui il comando passato (tutto ciò che viene dopo)
shift 2     # Rimuove i primi due argomenti ($1 e $2)
exec "$@"   # Esegue il comando rimanente