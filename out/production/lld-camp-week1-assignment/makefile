# Local Java commands
# Local Java commands
run1:
	javac NotifyDIPOCP.java && java NotifyDIPOCP

run2:
	javac MediaLSTIsp.java && java MediaLSTIsp

run3:
	javac InvoiceSRPOCP.java && java InvoiceSRPOCP

# Docker commands
build:
	docker build -t java-assignments .

run-docker1:
	docker rm Week1-Assignment-Java || true
	docker run --name Week1-Assignment-Java java-assignments java NotifyDIPOCP

run-docker2:
	docker rm Week1-Assignment-Java || true
	docker run --name Week1-Assignment-Java java-assignments java MediaLSTIsp

run-docker3:
	docker rm Week1-Assignment-Java || true
	docker run --name Week1-Assignment-Java java-assignments java InvoiceSRPOCP

run-all:
	docker rm Week1-Assignment-Java || true
	docker run --name Week1-Assignment-Java java-assignments