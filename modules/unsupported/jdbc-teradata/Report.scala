import scalax.io._
import scalax.file._
val tests = Path("target/surefire-reports/") * "TEST-*"

implicit val c=Codec.UTF8

val report = """
%s
  tests: %s successes: %s failures: %s
  testNames:
%s

"""
val out = Path("target/surefire-reports/fullSummary.txt")
out.delete()
var suc = 0
var fail = 0
tests.foreach { f =>
	val results = xml.XML.loadFile(f.path)
	val t = results \\ "testsuite"
	val name = t \ "@name"
  val failures = (t \ "@failures").text.toInt + (t \ "@errors").text.toInt
  val successes = (t \ "@tests").text.toInt - failures
  suc += successes
  fail += failures
  val cases = t \\ "testcase" map { c =>
    val name = (c \\ "@name").text

    if (c \\ "error" nonEmpty) "X   "+name
    else "    "+name
  }
  val testNames = cases mkString "\n"
  out append report.format(name.text.split("\\.").last, successes+failures, successes, failures, testNames)
}

out.append("Totals: tests: "+(suc+fail)+" successes: "+suc+" failures: "+fail)