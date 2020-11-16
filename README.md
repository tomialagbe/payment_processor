## How to run

######Setup the Environment
The following services are required to run the app
- Redis
- Zookeeper
- Kafka

A docker-compose file is provided that starts up Zookeeper, Kafka and Redis.
You can start them by running
 
````
docker-compose up
````

######Build The Applications
To build the applications run the following command from the projects root directory.
````
sh ./setup-dependencies.sh
````

#
####Starting the services
The services should be started on separate terminals.

######The API Server
To start the API Server (Source), run:
````
sh ./run-api.sh
````

######The Tokenizer service
To start the Tokenizer Service (Data Sink), run:
````
sh ./run-tokenizer.sh
````

######The Consumer service
To start the consumer service (Proof), run:
````
sh ./run-consumer.sh
````

#
####Testing
######Make a request
Now, you can send a card auth request to the API server which runs on http://localhost:1001.

The HTTP request url should be:
````
GET http://localhost:1001/api/auth
```` 

And the requuest body should look like this:
````
{
	"cardNumber": "1376137938413", 
	"expirationDate": "02/23",
	"cvv": "084"
}
````

