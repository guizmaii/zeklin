import sbt.TaskKey

object CustomTasks {

  lazy val copyFastOptJS = TaskKey[Unit]("copyFastOptJS", "Copy javascript files to target directory")

}
