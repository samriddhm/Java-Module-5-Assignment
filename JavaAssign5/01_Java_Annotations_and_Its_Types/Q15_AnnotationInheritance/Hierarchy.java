/**
 * Q15: a small class hierarchy used by InheritanceDemo.
 * (Package-private classes can share one file; only one public class per file.)
 */
public class Hierarchy {
}

@InheritedTag("from Animal")
@PlainTag("from Animal")
class Animal {
    @InheritedTag("on Animal.speak()")
    public void speak() { }
}

/** Dog has NO annotations written on it. */
class Dog extends Animal {
    @Override
    public void speak() { }    // overriding method: method annotations are NOT inherited
}

/** Puppy overrides the inherited annotation with its own value. */
@InheritedTag("from Puppy")
class Puppy extends Dog {
}

@InheritedTag("from interface Pet")
interface Pet { }

/** Cat implements an annotated interface - @Inherited does NOT work through interfaces. */
class Cat implements Pet { }
