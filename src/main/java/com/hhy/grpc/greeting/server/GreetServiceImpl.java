package com.hhy.grpc.greeting.server;

import com.proto.greet.*;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {
    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        Greeting greeting = request.getGreeting();
        String firstName = greeting.getFirstName();

        String result = "Hello " + firstName;
        GreetResponse response = GreetResponse.newBuilder()
                .setResult(result)
                .build();

        // send the response
        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }

    @Override
    public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver)  {
        Greeting greeting = request.getGreeting();
        try {
            for (int i = 0; i < 5; i++) {
                GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder()
                        .setResult("Hello " + greeting.getFirstName() + ", response number:" + i)
                        .build();
                Thread.sleep(1000L);
                responseObserver.onNext(response);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            responseObserver.onCompleted();;
        }





    }
}
