package pgb

import scala.sys.process._
import scala.util.Try

/**
  * @author kasonchan
  * @since Jan-2018
  */
object Pgb {

  lazy val gatlingVersion = "2.3.0"

  /**
    * Execute curl -fO to download gatling bundle.
    * @param version User specified version.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def dlgb(version: String = gatlingVersion): Try[Int] = Try {
    lazy val link = s"https://repo1.maven.org/maven2/io/gatling/highcharts/gatling-charts-highcharts-bundle/" +
      s"$version/gatling-charts-highcharts-bundle-$version-bundle.zip"
    s"curl -fO $link".!
  }

  /**
    * Unzip the downloaded gatling bundle.
    * @param version User specified version.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def unzipgb(version: String = gatlingVersion): Try[Int] = Try {
    s"unzip gatling-charts-highcharts-bundle-$version-bundle.zip".!
  }

  /**
    * Move unzipped Gatling bundle to current directory.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  private def mvgb: Try[Int] = {
    lazy val moveExitCode = Try {
      Seq("/bin/sh", "-c", "mv -fv gatling-charts-highcharts-bundle-2.3.0/* .").!
    }

    lazy val removeExitCode = Try {
      Seq("/bin/sh", "-c", "rm -rv user-files/simulations/*").!
    }

    for {
      m <- moveExitCode
      r <- removeExitCode
    } yield m + r
  }

  /**
    * Copy simulation files and directories to the corresponding direcotories.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  private def cpSims: Try[Int] = {
    lazy val dataExitCode = Try {
      Seq("/bin/sh", "-c", "cp -frv src/test/resources/data/* user-files/data").!
    }

    lazy val resourcesExitCode = Try {
      Seq("/bin/sh", "-c", "cp -frv src/test/resources/bodies/* user-files/bodies").!
    }

    lazy val scalaExitCode = Try {
      Seq("/bin/sh", "-c", "cp -frv src/test/scala/* user-files/simulations").!
    }

    for {
      d <- dataExitCode
      r <- resourcesExitCode
      s <- scalaExitCode
    } yield d + r + s
  }

  /**
    * Remove all Gatling bundle files and directories.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  private def cleanupGB: Try[Int] = {
    val gbDirectories = List("bin",
      "conf",
      "lib",
      "results",
      "user-files",
      "LICENSE",
      "gatling-charts-highcharts-bundle-*")

    Try {
      Seq("/bin/sh", "-c", s"rm -rv ${gbDirectories.mkString(" ")}").!
    }
  }

  /**
    * Remove all build file and Gatling bundle files and directories.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  private def cleanupBuild: Try[Int] = {
    lazy val rmBuild = Try {
      s"rm -rv ../${new java.io.File(".").getCanonicalFile.getName}.zip".!
    }

    for {
      r <- rmBuild
    } yield r
  }

}
