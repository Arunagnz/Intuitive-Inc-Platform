For Development purpose only

Start postgres instance

docker run --name postgres-db -e POSTGRES_PASSWORD=password -d -p 5432:5432 postgres

Start prometheus instance

docker run --name prometheus -p 9090:9090 -v C:\Users\aruna\Documents\Intuitve-inc-platform\prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus -d
