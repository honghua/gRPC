package com.hhy.grpc.calculator.server;

import com.proto.calculator.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {
    @Override
    public void sum(SumRequest sumRequest, StreamObserver<SumResponse> responseObserver) {
        SumResponse sumResponse = SumResponse.newBuilder()
                .setResult(sumRequest.getFirstNumber() + sumRequest.getSecondNumber())
                .build();

        responseObserver.onNext(sumResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void primeNumberDecomposition(PrimeRequest request, StreamObserver<PrimeResponse> responseObserver) {
        System.out.println("rpc prime decompose server request received");
        int number = request.getNumberToDecompose();
        int divisor = 2;
        while (divisor <= number) {
            if (number % divisor == 0) {
                PrimeResponse response = PrimeResponse.newBuilder()
                        .setResult(divisor)
                        .build();
                responseObserver.onNext(response);

                number /= divisor;
            } else {
                divisor++;
            }
        }

        /*
        loop over all possible factors
            1. if remains % factor: print
            2. else: factor + 1;
         */
        responseObserver.onCompleted();
    }

    @Override
    public void squareRoot(SquareRootRequest request, StreamObserver<SquareRootResponse> responseObserver) {
        Integer number = request.getNumber();

        if (number >= 0) {
            double numberRoot = Math.sqrt(number);
            responseObserver.onNext(
                    SquareRootResponse.newBuilder()
                    .setNumberRoot(numberRoot)
                    .build()
            );
            responseObserver.onCompleted();
        } else {
            // we construct the exception
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("the number sent is not positive. ")
                            .augmentDescription("the number sent: " + number)
                    .asRuntimeException()
            );
        }
    }
}
