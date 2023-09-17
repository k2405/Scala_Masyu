import scala.io.Source._
import scala.collection.mutable._
case class Puzzle(x:Int, y:Int, sol: Array[Array[Tile]]  ){
  val nr = 5
  private val filename = "src/ScalaAssignment/scala/puzzle_unsolved.txt"
   val width: Int = x
   val height: Int = y


   val tiles: Array[Array[Tile]] = sol



  def get_tiles() :Array[Array[Tile]] ={
    val value :Array[Array[Tile]] =Array.ofDim[Tile](height, width)



     value

  }

  def copyTiles(): Array[Array[Tile]] = {
    // function to deepcopy the class
    for (row <- tiles) yield {
      for (item <- row) yield item.copyTile()
    }
  }
  def lost():Boolean= {
    // function to evaluate if the board is wrong
    for (i <- 0 until height) {
      for (j <- 0 until width) {
        if (tiles(i)(j).inn_ring() && tiles(i)(j).dead_end()) return true
      }

    }
    return false
  }

  def find_move():Array[Int] = {
    // function to find a legal move that can be made

          for (i <- 0 until height) {
            for (j <- 0 until width) {
              if (!tiles(i)(j).crowded())

          if(tiles(i)(j).inn_ring()){


            if(!tiles(i)(j).up()&& !tiles(i)(j).upIllegal()){
              println(tiles(i)(j).paths(2))
              return Array[Int] (i,j,2)
            }
            if (tiles(i)(j).downMissing()) {
              println(tiles(i)(j).paths(1))
              return Array[Int](i, j, 1)
            }
            if (tiles(i)(j).leftMissing()) {
              println(tiles(i)(j).paths(0))

              return Array[Int](i, j, 0)
            }
            if (tiles(i)(j).rightMissing()) {
              println(tiles(i)(j).paths(3))

              return Array[Int](i, j, 3)
            }


          }
      }
    }
    return Array[Int] (-1,-1,-1)
  }


  def printBoard: Any = {
    // function to print the board with correct syntax
    println
    for (i <- 0 until height) {
      for (j <- 0 until width) {
        if (tiles(i)(j).isBlack) {
          print('┼')
        }
        else if (tiles(i)(j).isWhite) {
          if (tiles(i)(j).left) {
            print('╨')
          }
          else if (tiles(i)(j).down) {
            print('╡')
          }
          else {
            print(' ')
          }
        }
        else if (tiles(i)(j).left) {
          if (tiles(i)(j).down) {
            // print left + down
            print('┐')
          }
          else if (tiles(i)(j).up) {
            //print left + up
            print('┘')
          }
          else if (tiles(i)(j).right) {
            //print left + right
            print('─')
          }
          else{
            print(' ')
          }
        }
        else if (tiles(i)(j).up) {
          if (tiles(i)(j).down) {
            //print up+down
            print('│')
          }
          else if (tiles(i)(j).right) {
            //print up + right
            print('└')
          }
          else {
            print(' ')
          }
        }
        else if (tiles(i)(j).right) {
          // print down+right
          if(tiles(i)(j).down()) {
            print('┌')
          }
          else{
            print(' ')
          }
        }
        else {
          print(' ')
        }

      }
      println
    }

  }

  def boardString: String = {
    // function that return a string of the board to be delivered in file
    var str: String = ""
    for (i <- 0 until height) {
      for (j <- 0 until width) {

        str = addCharToBoardString(str, i, j)
      }
      str = str + "\n"
    }
    str
  }

  def addCharToBoardString(str: String, i: Int, j: Int): String = {
    // function that ands a char to the board string that mach the content of the tile
    if (tiles(i)(j).isBlack) {
      str + "┼"
    }
    else if (tiles(i)(j).isWhite) {
      if (tiles(i)(j).left) {
        str + "╨"
      }
      else if (tiles(i)(j).down) {
        str + "╡"
      }
      else {
        str + " "
      }
    }
    else if (tiles(i)(j).left) {
      if (tiles(i)(j).down) {
        // print left + down
        str + "┐"
      }
      else if (tiles(i)(j).up) {
        //print left + up
        str + "┘"
      }
      else if (tiles(i)(j).right) {
        //print left + right
        str + "─"
      }
      else {
        str + " "
      }
    }
    else if (tiles(i)(j).up) {
      if (tiles(i)(j).down) {
        //print up+down
        str + "│"
      }
      else if (tiles(i)(j).right) {
        //print up + right
        str + "└"
      }
      else {
        str + " "
      }
    }
    else if (tiles(i)(j).right) {
      // print down+right
      str + "┌"
    }
    else {
      str + " "

    }
  }
  private def count_dots: Int = {
    // function who count the dots on the board
    var count = 0
    for (ii <- 0 until height) {
      for (j <- 0 until width) {
        if (!tiles(ii)(j).isEmpty) {
          count = count + 1
        }
      }
    }
    count
  }


   def draw_down(legality: Int, x: Int, y: Int): Unit = {
     // function that draws a line from a tile to the tile below
    if(!tiles(y)(x).down && !tiles(y)(x).crowded && legality == 1){

      tiles(y)(x).paths(1)= Line.Placed
      tiles(y+1)(x).paths(2)=Line.Placed
    }
    if (!tiles(y)(x).downIllegal  && legality == -1) {

      tiles(y)(x).paths(1) = Line.Illegal
      tiles(y + 1)(x).paths(2) = Line.Illegal
    }


  }

   def draw_up(legality: Int, x: Int, y: Int) : Unit = {
     // function that draws a line from a tile to the tile above
    if (!tiles(y)(x).up && !tiles(y)(x).crowded && legality == 1) {

      tiles(y)(x).paths(2) = Line.Placed
      tiles(y - 1)(x).paths(1) = Line.Placed
    }
    if (!tiles(y)(x).upIllegal && legality == -1) {

      tiles(y)(x).paths(2) = Line.Illegal
      tiles(y - 1)(x).paths(1) = Line.Illegal
    }
  }

   def draw_left(legality: Int, x: Int, y: Int): Unit = {
     // function that draws a line from a tile to the tile to the left
    if (!tiles(y)(x).left && !tiles(y)(x).crowded && legality == 1) {

      tiles(y)(x).paths(0) = Line.Placed
      tiles(y)(x - 1).paths(3) = Line.Placed
    }
    if (!tiles(y)(x).leftIllegal && legality == -1) {

      tiles(y)(x).paths(0) = Line.Illegal
      tiles(y)(x - 1).paths(3) = Line.Illegal
    }
  }

   def draw_right(legality: Int, x: Int, y: Int):  Unit = {
     // function that draws a line from a tile to the tile to the left
    if (!tiles(y)(x).right && !tiles(y)(x).crowded && legality == 1) {

      tiles(y)(x).paths(3) = Line.Placed
      tiles(y)(x +1 ).paths(0) = Line.Placed
    }
    if (!tiles(y)(x).rightIllegal && legality == -1) {

      tiles(y)(x).paths(3) = Line.Illegal
      tiles(y)(x + 1).paths(0) = Line.Illegal
    }
  }


  def print_ugly: Unit = {
    // function that print the board for debugging, contains information that is not in the solution string


    println("Left")
    for (ii <- 0 until height) {
      for (j <- 0 until width) {

        if(tiles(ii)(j).leftMissing){
          print(" 0 ")
        }
        if (tiles(ii)(j).leftIllegal) {
          print(" x ")
        }
        if (tiles(ii)(j).left) {
          print(" ─ ")
        }
      }
      println
    }
    println("Down")
    for (ii <- 0 until height) {
      for (j <- 0 until width) {

        if (tiles(ii)(j).downMissing) {
          print(" 0 ")
        }
        if (tiles(ii)(j).downIllegal) {
          print(" x ")
        }
        if (tiles(ii)(j).down) {
          print(" | ")
        }
      }
      println
    }

    println("Right")
    for (ii <- 0 until height) {
      for (j <- 0 until width) {

        if (tiles(ii)(j).rightMissing) {
          print(" 0 ")
        }
        if (tiles(ii)(j).rightIllegal()) {
          print(" x ")
        }
        if (tiles(ii)(j).right) {
          print(" ─ ")
        }
      }
      println()
    }
    println("UP")
    for (ii <- 0 until height) {
      for (j <- 0 until width) {

        if (tiles(ii)(j).upMissing()) {
          print(" 0 ")
        }
        if (tiles(ii)(j).upIllegal()) {
          print(" x ")
        }
        if (tiles(ii)(j).up()) {
          print(" | ")
        }
      }
      println()
    }
  }


  private def border_Top(x:Int): Array[Array[Tile]] = {
    if(x>=width) {
      return tiles
    }
    // function that creates the top border, tells the top tiles that it is illegal to move up
      val newtiles = tiles

    newtiles(0)(x).paths(2)=Line.Illegal
    return border_Top(x+1)


  }

  private def border_Bottom(): Unit = {
    // function that creates the bottom border, tells the bottom tiles that it is illegal to move down
    for (i <- 0 until width) {
      tiles(height-1)(i).paths(1) = Line.Illegal
    }
  }

  private def border_Left(): Unit = {
    // function that creates the left border, tells the left tiles that it is illegal to move left
    for (i <- 0 until height) {
      tiles(i)(0).paths(0) = Line.Illegal
    }
  }

  private def border_Right(): Unit = {
    // function that creates the right border, tells the bottom tiles that it is illegal to move right
    for (i <- 0 until height) {
      tiles(i)(width-1).paths(3) = Line.Illegal
    }
  }

  def borders():Unit = {
    // function that creates the borders, calls function that does that
    border_Left()
    border_Right()
    border_Top(0)
    border_Bottom()
  }
  private def set_up_black(x: Int, y: Int): Unit = {
    // function that checks if the black dot in the given tile is next to an other black dot
    // if so defines it illegal to move between them.
    if(tiles(y)(x).rightMissing() && tiles(y)(x+1).isBlack){
      draw_right(-1,x,y)
    }
    if (tiles(y)(x).downMissing() && tiles(y+1)(x).isBlack) {
      draw_down(-1, x, y)
    }
  }
  private def set_up_white_vertical(x: Int, y: Int): Unit = {
    // checks if there is 3 white dots next to each other vertical
    if((tiles(y)(x).downMissing() && tiles(y+1)(x).isWhite)&& (tiles(y+1)(x).downMissing() && tiles(y+2)(x).isWhite)){
      draw_down(-1,x,y)
    }
  }

  private def set_up_white_horizontal(x: Int, y: Int): Unit = {
 // checks if there is 3 white dots next to each other horizontaly
    if ((tiles(y)(x).leftMissing() && tiles(y)(x - 1).isWhite) && (tiles(y)(x - 1).leftMissing() && tiles(y)(x - 2).isWhite)) {
      draw_left(-1, x, y)

    }
  }
  private def set_up_black_line(x: Int, y: Int):Unit = {
    // checks if there are the following pattern in any direction * _ o o  from this black dot, in that case defines the move it that direction illegal
    if(tiles(y)(x).rightMissing() && tiles(y)(x+1).rightMissing() &&  tiles(y)(x+2).rightMissing() && (tiles(y)(x+2).isWhite && tiles(y)(x+3).isWhite )) draw_right(-1,x,y)
  if (tiles(y)(x).leftMissing() && tiles(y)(x - 1).leftMissing() && tiles(y)(x - 2).leftMissing() && (tiles(y)(x - 2).isWhite && tiles(y)(x - 3).isWhite)) draw_left(-1, x, y)
    if (tiles(y)(x).upMissing() && tiles(y - 1)(x).upMissing() && tiles(y - 2)(x).upMissing() && (tiles(y - 2)(x).isWhite && tiles(y - 3)(x).isWhite)) draw_up(-1, x, y)
    if (tiles(y)(x).downMissing() && tiles(y + 1)(x).downMissing() && tiles(y + 2)(x).downMissing() && (tiles(y + 2)(x).isWhite && tiles(y + 3)(x).isWhite)) draw_down(-1, x, y)
  }


  private def set_up_black_diagonal_whites(x: Int, y: Int): Unit = {
    // check for
    //    *
    //  o _ o
    // if so defines the move in that direction illegal

    if(tiles(y)(x).downMissing()&&tiles(y)(x).leftMissing()&&tiles(y)(x).rightMissing()){
      if(tiles(y+1)( x-1).isWhite  & tiles(y+1)(x+1).isWhite){
        draw_down(-1,x,y)
      }
    }
    if (tiles(y)(x).upMissing() && tiles(y)(x).leftMissing() && tiles(y)(x).rightMissing()) {
      if (tiles(y - 1)(x - 1).isWhite & tiles(y - 1)(x + 1).isWhite) {
        draw_up(-1, x, y)
      }
    }
    if (tiles(y)(x).leftMissing() && (tiles(y)(x).downMissing() && tiles(y)(x).upMissing())) {
      if (tiles(y + 1)(x - 1).isWhite & tiles(y - 1)(x - 1).isWhite) {
        draw_left(-1, x, y)
      }
    }
    if (tiles(y)(x).rightMissing() && (tiles(y)(x).downMissing() && tiles(y)(x).upMissing())) {
      if (tiles(y + 1)(x + 1).isWhite & tiles(y - 1)(x + 1).isWhite) {
        draw_right(-1, x, y)
      }
    }
  }

  def won(): Boolean = {
    // checks if the board is completed
    for (ii <- 0 until height) {
      for (j <- 0 until width) {
        if(!tiles(ii)(j).isEmpty){



       //   if (tiles(ii)(j).down() && circle(j, ii+1, j, ii, count_dots, 2) == 1) {
         //   println("w,d")
        //    return true
         // }
       //   if (tiles(ii)(j).up() && circle(j, ii+1, j, ii , count_dots, 2) == 1)  {
         //   println("w,u")
          //  return true
         // }
          if (tiles(ii)(j).left() ) {

          }
         // if (tiles(ii)(j).left() && circle(j, ii, j+1 , ii, count_dots, 0) == 1) {
           // println("w,l")
            //return true
          //}
          if (tiles(ii)(j).left() && circle(j-1 , ii, j, ii, count_dots, 0) == 1) {
            println("w,r")
            return true
          }
        //  if (tiles(ii)(j).right() && circle(j+1, ii, j, ii, count_dots, 0) == 1) {
          //  println("w,r")
            //return true
          //}


        }
      }

    }
    false
  }

  def set_Up():Unit ={
    // calls all set_up functions in the relevant tiles
    for (ii <- 0 until height) {
      for (j <- 0 until width) {
        if(tiles(ii)(j).isBlack) {
          set_up_black(j,ii)
          set_up_black_diagonal_whites(j,ii)
          set_up_black_line(j,ii)
        }
        if(tiles(ii)(j).isWhite) set_up_white_vertical(j,ii)
        if(tiles(ii)(j).isWhite) set_up_white_horizontal(j,ii)

      }
    }
  }

  private def illegal_black_dot(x: Int, y: Int):Unit = {
    // checks if any move is illegal for a black dot
    if((tiles(y)(x).downMissing() && ((tiles(y+1)(x).downIllegal()) | (tiles(y+1)(x).left() | tiles(y+1)(x).right())))) draw_down(-1,x, y)
    if(tiles(y)(x).upMissing() && (tiles(y-1)(x).upIllegal() | tiles(y-1)(x).left() | tiles(y-1)(x).right())) draw_up(-1,x, y)
    if((tiles(y)(x).leftMissing() && ((tiles(y)(x-1).leftIllegal()) | (tiles(y)(x-1).up() | tiles(y)(x-1).down())))) draw_left(-1,x, y)
    if(tiles(y)(x).rightMissing() && (tiles(y)(x+1).rightIllegal() | tiles(y)(x+1).up() | tiles(y)(x+1).down())) draw_right(-1,x, y)
    // check if circle is formed
    if(tiles(y)(x).downMissing()  && circle(x,y+2,x,y,count_dots,2)== -1) draw_down(-1,x, y)
    if(tiles(y)(x).upMissing()  && circle(x,y-2,x,y,count_dots,1)== -1) draw_up(-1,x, y)
    if (tiles(y)(x).leftMissing() && circle(x-2, y, x, y, count_dots, 3) == -1) draw_left(-1, x, y)
    if (tiles(y)(x).rightMissing() && circle(x-2, y, x, y, count_dots, 0) == -1) draw_right(-1, x, y)

  }
  private def legal_crowded(x: Int, y: Int):Unit = {
    // called when there are two illegal moves in the tile and one placed, this function setts the last move to placed


    if (tiles(y)(x).leftMissing()) draw_left(1, x, y)
    if (tiles(y)(x).downMissing()) draw_down(1, x, y)
    if (tiles(y)(x).upMissing()) draw_up(1, x, y)
    if (tiles(y)(x).rightMissing()) draw_right(1, x, y)


  }
  private def illegal_white_dots(x: Int, y: Int):Unit = {
    // checks if any move is illegal for a white dot
    if(tiles(y)(x).down() && tiles(y+1)(x).down()) draw_up(-1,x, y-1)
    if(tiles(y)(x).up() && tiles(y-1)(x).up()) draw_down(-1,x, y+1)
    if(tiles(y)(x).right() && tiles(y)(x+1).right()) draw_left(-1,x-1, y)
    if(tiles(y)(x).left() && tiles(y)(x-1).left()) draw_right(-1,x+1, y)
    if(tiles(y)(x).left()) {
      draw_up(-1,x, y)
      draw_down(-1,x,y)

    }
    if (tiles(y)(x).right()) {
      draw_up(-1, x, y)
      draw_down(-1, x, y)

    }
    if (tiles(y)(x).up()) {
      draw_left(-1, x, y)
      draw_right(-1, x, y)

    }
    if (tiles(y)(x).down()) {
      draw_left(-1, x, y)
      draw_right(-1, x, y)

    }
    if (tiles(y)(x).leftIllegal()) draw_right(-1, x, y)
    if (tiles(y)(x).downIllegal()) draw_up(-1, x, y)
    if (tiles(y)(x).upIllegal) draw_down(-1, x, y)
    if (tiles(y)(x).rightIllegal) draw_left(-1, x, y)
    if ( ( !tiles(y)(x).leftIllegal && tiles(y)(x-1).left) && ( !tiles(y)(x).rightIllegal() && tiles(y)(x+1).right)){
      draw_left(-1, x, y)
      draw_right(-1, x, y)
    }
    if ((!tiles(y)(x).upIllegal && tiles(y-1)(x).up) && (!tiles(y)(x).downIllegal && tiles(y+1)(x).down)) {
      draw_up(-1, x, y)
      draw_down(-1, x, y)
    }
  }
  private def illegal_crowded(x: Int, y: Int):Unit = {
     // called when a tile has two placed moves this function defines remaining moves to illegal.

    if(tiles(y)(x).leftMissing()) draw_left(-1,x,y)
    if(tiles(y)(x).downMissing()) draw_down(-1,x,y)
    if(tiles(y)(x).upMissing()) draw_up(-1,x,y)
    if(tiles(y)(x).rightMissing()) draw_right(-1,x,y)
  }
  private def legal_black(x: Int, y: Int):Unit = {
    // called for every black dot, checks if a move is illegal that makes an other move forced
    if(tiles(y)(x).leftIllegal()){
      draw_right(1,x,y)
      draw_right(1,x+1,y)
    }
    if (tiles(y)(x).rightIllegal()) {
      draw_left(1, x, y)
      draw_left(1, x - 1, y)
    }
    if (tiles(y)(x).downIllegal()) {
      draw_up(1, x, y)
      draw_up(1, x, y-1)
    }
    if (tiles(y)(x).upIllegal()) {
      draw_down(1, x, y)
      draw_down(1, x, y + 1)
    }
  }
  private def circle(start_x: Int, start_y: Int, Current_x: Int, current_y: Int, Remaining_dots: Int, Current_direction: Int): Int={
    // recursive function that determine of from start position one there is a line to the current position and if so if it passes trough every dot
    if(!tiles(current_y)(Current_x).isEmpty) {

      if (start_y == current_y && start_x == Current_x) {
        if (Remaining_dots ==0) {
          return 1
        }
        else{

        return -1
        }
      }


        if (tiles(current_y)(Current_x).up() && 2 != Current_direction) return circle(start_x, start_y, Current_x, current_y-1, Remaining_dots-1, 1)
        if (tiles(current_y)(Current_x).down() && 1 != Current_direction) return circle(start_x, start_y, Current_x, current_y+1, Remaining_dots-1, 2)
        if (tiles(current_y)(Current_x).right() && 3 != Current_direction) return circle(start_x, start_y, Current_x+1, current_y, Remaining_dots-1, 0)
        if (tiles(current_y)(Current_x).left() && 0 != Current_direction) return circle(start_x , start_y, Current_x-1, current_y, Remaining_dots-1, 3)
      if (!tiles(current_y)(Current_x).crowded()) return 0

    }
    else {
        if (start_y == current_y & start_x == Current_x) {

        if (Remaining_dots == 0) {
          return 1
        }
        else{


          return -1
        }
      }

        if (tiles(current_y)(Current_x).up() && 2 != Current_direction) return circle(start_x, start_y , Current_x, current_y-1, Remaining_dots, 1)
        if (tiles(current_y)(Current_x).down() && 1 != Current_direction) return circle(start_x, start_y, Current_x, current_y +1, Remaining_dots, 2)
        if (tiles(current_y)(Current_x).right() && 3 != Current_direction) return circle(start_x, start_y, Current_x+1, current_y, Remaining_dots, 0)
        if (tiles(current_y)(Current_x).left() && 0 != Current_direction) return circle(start_x, start_y, Current_x-1, current_y, Remaining_dots, 3)
      if (!tiles(current_y)(Current_x).crowded()) return 0


    }


     0
  }
  private def avoid_circle_one_move(x: Int, y: Int): Unit={
    // called in tiles if any move results in an mini circle if so sets this move to illegal

    if (tiles(y)(x).downMissing() && circle(x, y , x, y+1, count_dots, 2) == -1) draw_down(-1, x, y)
    if (tiles(y)(x).upMissing() && circle(x, y, x, y-1, count_dots, 1) == -1) draw_up(-1, x, y)
    if (tiles(y)(x).leftMissing() && circle(x, y, x-1, y, count_dots, 3) == -1) draw_left(-1, x, y)
    if (tiles(y)(x).rightMissing() && circle(x , y, x+1, y, count_dots, 0) == -1) draw_right(-1, x, y)
  }


  def illegal_moves():Boolean={
    // calls functions in relevant tiles to determine if any moves are illegal
    for (ii <- 0 until height) {
      for (j <- 0 until width) {
        if(tiles(ii)(j).isBlack) illegal_black_dot(j,ii)
        if(tiles(ii)(j).isWhite) illegal_white_dots(j,ii)
        if( tiles(ii)(j).crowded() | tiles(ii)(j).dead_end()) illegal_crowded(j,ii)
        if(!tiles(ii)(j).crowded() && tiles(ii)(j).inn_ring()) avoid_circle_one_move(j,ii)

      }

      }
     true
  }
  def legal_moves():Unit= {
    // calls functions in relevant tiles to determine if any moves are forced
    for (ii <- 0 until height) {
      for (j <- 0 until width) {
        if(tiles(ii)(j).isBlack) legal_black(j,ii)
        if(tiles(ii)(j).Illegal_crowded() && tiles(ii)(j).inn_ring()) legal_crowded(j,ii)

      }
    }
  }

  def createAlteredBoard(board: Puzzle, arr:Array[Array[Array[Boolean]]]): Puzzle = {
    val newBoard = board
    for(i<-0 until height){
      for (j<-0 until width){
        for (d<-0 until 4){
          if(arr(i)(j)(d)){
            if(newBoard.tiles(i)(j).paths(d) != Line.Illegal && !newBoard.tiles(i)(j).crowded()){
              newBoard.tiles(i)(j).paths(d) = Line.Placed
            }
          }
        }
      }
    }
     newBoard
  }

  def createPathCombination(combination: Int): Array[Boolean] = {
    if(combination==0){
       Array[Boolean](false,false,false,false)
   }
    else if(combination==1){
       Array[Boolean](true,false,false,false)
    }
    else if(combination==2){
       Array[Boolean](false,true,false,false)
    }
    else if(combination==3){
       Array[Boolean](false,false,true,false)
    }
    else if (combination == 4) {
       Array[Boolean](false, false, false, true)
    }
    else if (combination == 5) {
       Array[Boolean](true, true, false, false)
    }
    else if (combination == 6) {
       Array[Boolean](true, false, true, false)
    }
    else if (combination == 7) {
       Array[Boolean](true, false, false, true)
    }
    else if (combination == 8) {
       Array[Boolean](false, true, true, false)
    }
    else if (combination == 9) {
       Array[Boolean](false, true, false, true)
    }
    else if (combination == 10) {
       Array[Boolean](false, false, true, true)
    }
    else{
       Array[Boolean](true, true, true, true)
    }
  }
  def illegalize(): Puzzle = {
    // called in search when there are no moves possible
    // this function sett all moves to illegal to tel the lower function call that this path is illegal or the move possible due to depth
    for (i <- 0 until height) {
      for (j <- 0 until width) {
        for (h <- 0 until 4){
          if(tiles(i)(j).paths(h)==Line.Missing) tiles(i)(j).paths(h)= Line.Illegal
        }
      }
      }
    return new Puzzle(width, height, tiles)
  }
}

