package com.hhy.grpc.calculator.client;

import com.proto.calculator.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

public class CalculatorClient {
    private static CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient;

    public static void main(String[] args) {
        System.out.println("I'm sum client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);
        System.out.println("calling rpc sum");
        printSum(1, 3);

        for (int num : Arrays.asList(24, 34, 349757351)) {
            System.out.println("primeDecomposing for " + num);
            printPrimeDecompose(num);
        }

        new CalculatorClient().doErrorCall(channel);

        channel.shutdown();
    }

    private static void printSum(int firstNumber, int secondNumber) {
        SumRequest request = SumRequest.newBuilder()
                .setFirstNumber(firstNumber)
                .setSecondNumber(secondNumber)
                .build();

        SumResponse sumResponse = calculatorClient.sum(request);
        System.out.println(request.getFirstNumber() + " + " + request.getSecondNumber() + " = " + sumResponse.getResult());
    }

    private static void printPrimeDecompose(int number) {
        PrimeRequest primeRequest = PrimeRequest.newBuilder()
                .setNumberToDecompose(number)
                .build();

        Iterator<PrimeResponse> primeResponse = calculatorClient.primeNumberDecomposition(primeRequest);
        primeResponse.forEachRemaining(
                response -> {
                    System.out.println(response.getResult());
                }
        );
    }

    private void doErrorCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);
        int number = -1;

        try {
            stub.squareRoot(SquareRootRequest.newBuilder()
                    .setNumber(number)
                    .build());
        } catch (StatusRuntimeException e) {
            System.out.println("Got an exception for square root!");
            e.printStackTrace();
        }

    }
}
