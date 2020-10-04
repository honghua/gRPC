package com.hhy.grpc.greeting.client;

import com.proto.dummy.DummyServiceGrpc;
import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Iterator;

public class GreetingClient {
    public static void main(String[] args) {
        System.out.println("Hello I'm a gRPC Client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        System.out.println("Creating stub");

        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);


        GreetManyTimesRequest greetRequest = GreetManyTimesRequest.newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("Harry").build())
                .build();

        Iterator<GreetManyTimesResponse> greetResponse = greetClient.greetManyTimes(greetRequest);
//        while (greetResponse.hasNext()) {
//            System.out.println(greetResponse.next().getResult());
//        }
        greetResponse.forEachRemaining(
                greetManyTimesResponse -> {
                    System.out.println(greetManyTimesResponse.getResult());
                }
        );


        System.out.println("Shutting down channel");
        channel.shutdown();
    }
}
