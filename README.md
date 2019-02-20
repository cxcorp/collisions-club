# collisions-club
Microservice for generating strings which all produce the same Java hashCode. Note that the generated output is almost always outside the ASCII range.

## Why?
`¯\_(ツ)_/¯`

## The formula
The generator abuses the fact that in Java, `String::hashCode` essentially sums every character's value, multiplying by `31` at each iteration.

Knowing this, we can trivially generate colliding strings. For example, for four characters, the formula would be:

```java
arr[0] = 2114 - a
arr[1] = a * 31 - b
arr[2] = b * 31 - c
arr[3] = c * 31
```

where

`0 <= c <= b <= a <= 2114`

## Try it
The service is up and running at this address:

https://collisions-club.herokuapp.com/generate?string_length=4&count=5

You can also execute this Java fiddle to see that all of the strings returned indeed have the same `hashCode`:

https://www.tutorialspoint.com/viewproject.php?URL=compile_java8_online.php&PID=0Bw_CjBb95KQMSWpDWVdGZHBzX3M

## API Endpoints
### `GET /generate`
Generates specified amount of strings which all produce the same `hashCode` in Java.

| Parameter | Description | Type |
|-----------|-------------|------|
| count | Amount of strings to generate. Must be inside range 1...1024 (inclusive). | int32 |
| string_length | Length of strings to generate (amount of UTF-16 code units, no guarantees about the byte count of the UTF-8 output). Currently only value supported is 4. | int32 |
| seed | (*Optional*) The seed to use for the random number generator. | int64 |

#### Example
Request

```
GET /generate?string_length=4&count=5&seed=1245125551
```

Response

```
GET /generate?string_length=4&count=5&seed=1245125551

{
  "data": [
    "ů퀍櫶⾶",
    "U䝙䋘",
    "˅꛴徠式",
    "Ł틢뮡뉾",
    "ج㻞㝐జ"
  ]
}

```

### `GET /hashes`
Gets the hashes produced by the specified string lengths.

#### Example
Request

```
GET /hashes
```

Response

```
{
  "data": [
    {
      "string_length": 4,
      "hash": 62978174
    }
  ]
}
```

## Download
See [Releases](https://github.com/cxcorp/collisions-club/releases).

## Running
1. Build or [download](https://github.com/cxcorp/collisions-club/releases) the `jar`.
2. Run the jar: `java -jar collisions-club-<version>.jar`.

## Building
1. Download or install [Apache Maven](https://maven.apache.org/install.html).
2. Clone or download this repository.
3. In the root folder, execute `mvn clean package`. This produces `collisions-club-<version>.jar` in the `target` folder.

## License
collisions-club is licensed under the MIT License. See `LICENSE`.

collisions-club uses third party libraries that are distributed under their own tems. See `LICENSE-3RD-PARTY`.
