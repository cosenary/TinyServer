Build and run this example 

```
$ ./gradlew :examples:ex01-spring:run
```

Testing it

```bash
$ curl -s -w '\n' '127.0.0.1:8200/temperatures/Sao-Paulo' | python -m json.tool
{
  "status": "200",
  "message": "OK",
  "data": {
    "temperature": 19,
    "city": "Sao-Paulo"
  }
}
```