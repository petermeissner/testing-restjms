# Batch script to add the JMS configuration to Wildfly
# Start batching commands
batch

# Configure the JMS test queue
jms-queue add --queue-address=inbound --entries=queue/inbound,java:jboss/exported/jms/queue/inbound
jms-queue add --queue-address=outbound --entries=queue/outbound,java:jboss/exported/jms/queue/outbound

# Run the batch commands
run-batch

# Reload the server configuration
reload
