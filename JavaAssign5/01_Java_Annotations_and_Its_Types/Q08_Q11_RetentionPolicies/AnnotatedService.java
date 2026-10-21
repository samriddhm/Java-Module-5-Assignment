/** Q8/Q11: one class carrying all three kinds of annotation. */
@SourceLevel("only the compiler sees me")
@ClassLevel("I live in the .class file but not at runtime")
@RuntimeLevel("reflection can read me")
public class AnnotatedService {
}
