FROM golang:1.14

WORKDIR /go/src/server
COPY . .

RUN go get -d -v ./...
RUN go install -v ./...

#CMD ["server"]