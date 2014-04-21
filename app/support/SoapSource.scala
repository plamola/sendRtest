package support

/**
 * Author: matthijs
 * Created on: 19 Apr 2014.
 */
abstract trait SoapSource {
  def getNrOfLines: Long

  def getCurrentFileName: String

  def getNextLine: String
}