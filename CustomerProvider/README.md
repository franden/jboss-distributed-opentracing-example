# Build
mvn clean package && docker build -t de.franden.opentracing/CustomerProvider .

# RUN

docker rm -f CustomerProvider || true && docker run -d -p 8080:8080 -p 4848:4848 --name CustomerProvider de.franden.opentracing/CustomerProvider 