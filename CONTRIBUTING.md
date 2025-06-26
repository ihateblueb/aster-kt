# Contributing

## Code Style

Make sure your editor is following the `.editorconfig` so you don't have to worry about spacing and tabs.

Avoid unneeded brackets when possible, for example:

```kotlin
if (RelationshipService.eitherBlocking(user.id.toString(), author.id.toString()))
	return false

// rather than

if (RelationshipService.eitherBlocking(user.id.toString(), author.id.toString())) {
	return false
}
```

```kotlin
suspend fun get(where: Op<Boolean>): UserEntity? = suspendTransaction {
	UserEntity
		.find { where }
		.singleOrNull()
}

// rather than

suspend fun get(where: Op<Boolean>): UserEntity? {
	return suspendTransaction {
		UserEntity
			.find { where }
			.singleOrNull()
	}
}
```

If there's enough potential reuse for some code or potential for expansion (example: TimeService at the time of writing
has one method, but is likely to be extended for further utilities), create a service class for it. Otherwise, create a
util.

## Tips

Jetbrains Exposed (the ORM used) doesn't persist joins outside the current transaction context. That's why NoteService
will return a Note rather than a NoteEntity. The Note object will persist all the values given to it, and they will be
usable outside the transaction context. You may want to do this for other services, use NoteService as a point of
reference for that.

