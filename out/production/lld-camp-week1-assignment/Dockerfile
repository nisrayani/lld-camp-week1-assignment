FROM eclipse-temurin:11-jdk

WORKDIR /app

# Copy the Java source files and makefile
COPY InvoiceSRPOCP.java .
COPY MediaLSTIsp.java .
COPY NotifyDIPOCP.java .
COPY makefile .

# Compile Java files
RUN javac InvoiceSRPOCP.java
RUN javac MediaLSTIsp.java
RUN javac NotifyDIPOCP.java

# Default command to run all programs
CMD echo "Running Program 1 (NotifyDIPOCP):" && \
    java NotifyDIPOCP && \
    echo "\nRunning Program 2 (MediaLSTIsp):" && \
    java MediaLSTIsp && \
    echo "\nRunning Program 3 (InvoiceSRPOCP):" && \
    java InvoiceSRPOCP