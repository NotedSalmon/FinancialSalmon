mvn clean install
docker build -t financialsalmon-payara .
docker-compose up --remove-orphans
