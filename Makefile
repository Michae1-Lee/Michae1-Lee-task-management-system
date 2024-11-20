run:
	docker-compose build
	docker-compose up -d
test:
	docker-compose up test