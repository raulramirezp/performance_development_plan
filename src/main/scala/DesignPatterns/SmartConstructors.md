# Smart Constructors

Sometimes you need guarantees about the values in your program beyond what can be accomplished with the usual type system checks.
Smart constructors can be used for this purpose.

Lets say that we have an Email class as follows: 
```
case class Email(address: String)
```
The problem with Email is that address can contain any value of type String, even if they are not valid email addresses

Smart Constructors is one solution for this: instead of normal constructors, we force construction through 
"smart" functions that only return Email instances when the input passes validation.
```
val emailPattern: Regex = """^(\w+)@(\w+(.\w+)+)$""".r

sealed abstract case class Email private(address: String)

object Email {
  def fromString(value: String): Option[Email] =
    value match
      case emailPattern(localPart, domainName, _) => Option(new Email(value){})
      case _ =>  None
}
```

- With sealed, we disallow attempts to extends Email from outside this source file.
- With abstract, we disallow construction through new Email().
  With abstract, copy() and apply() are not automatically generated. We don't have to worry about them.
- Email.fromString is the only way to construct an Email from outside of this source file.
- With private constructor construction through new Email(value) can only takes place within the current source file.

___ 
References:
- https://wiki.haskell.org/Smart_constructors
- https://tuleism.github.io/blog/2020/scala-smart-constructors/
- https://davegurnell.com/articles/smartypants/
