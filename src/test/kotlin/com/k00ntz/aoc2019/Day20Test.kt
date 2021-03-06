package com.k00ntz.aoc2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day20Test {

    val input1String = "         A           \n" +
            "         A           \n" +
            "  #######.#########  \n" +
            "  #######.........#  \n" +
            "  #######.#######.#  \n" +
            "  #######.#######.#  \n" +
            "  #######.#######.#  \n" +
            "  #####  B    ###.#  \n" +
            "BC...##  C    ###.#  \n" +
            "  ##.##       ###.#  \n" +
            "  ##...DE  F  ###.#  \n" +
            "  #####    G  ###.#  \n" +
            "  #########.#####.#  \n" +
            "DE..#######...###.#  \n" +
            "  #.#########.###.#  \n" +
            "FG..#########.....#  \n" +
            "  ###########.#####  \n" +
            "             Z       \n" +
            "             Z       "

    val input2String = "                   A               \n" +
            "                   A               \n" +
            "  #################.#############  \n" +
            "  #.#...#...................#.#.#  \n" +
            "  #.#.#.###.###.###.#########.#.#  \n" +
            "  #.#.#.......#...#.....#.#.#...#  \n" +
            "  #.#########.###.#####.#.#.###.#  \n" +
            "  #.............#.#.....#.......#  \n" +
            "  ###.###########.###.#####.#.#.#  \n" +
            "  #.....#        A   C    #.#.#.#  \n" +
            "  #######        S   P    #####.#  \n" +
            "  #.#...#                 #......VT\n" +
            "  #.#.#.#                 #.#####  \n" +
            "  #...#.#               YN....#.#  \n" +
            "  #.###.#                 #####.#  \n" +
            "DI....#.#                 #.....#  \n" +
            "  #####.#                 #.###.#  \n" +
            "ZZ......#               QG....#..AS\n" +
            "  ###.###                 #######  \n" +
            "JO..#.#.#                 #.....#  \n" +
            "  #.#.#.#                 ###.#.#  \n" +
            "  #...#..DI             BU....#..LF\n" +
            "  #####.#                 #.#####  \n" +
            "YN......#               VT..#....QG\n" +
            "  #.###.#                 #.###.#  \n" +
            "  #.#...#                 #.....#  \n" +
            "  ###.###    J L     J    #.#.###  \n" +
            "  #.....#    O F     P    #.#...#  \n" +
            "  #.###.#####.#.#####.#####.###.#  \n" +
            "  #...#.#.#...#.....#.....#.#...#  \n" +
            "  #.#####.###.###.#.#.#########.#  \n" +
            "  #...#.#.....#...#.#.#.#.....#.#  \n" +
            "  #.###.#####.###.###.#.#.#######  \n" +
            "  #.#.........#...#.............#  \n" +
            "  #########.###.###.#############  \n" +
            "           B   J   C               \n" +
            "           U   P   P               "

    val input3String = "             Z L X W       C                 \n" +
            "             Z P Q B       K                 \n" +
            "  ###########.#.#.#.#######.###############  \n" +
            "  #...#.......#.#.......#.#.......#.#.#...#  \n" +
            "  ###.#.#.#.#.#.#.#.###.#.#.#######.#.#.###  \n" +
            "  #.#...#.#.#...#.#.#...#...#...#.#.......#  \n" +
            "  #.###.#######.###.###.#.###.###.#.#######  \n" +
            "  #...#.......#.#...#...#.............#...#  \n" +
            "  #.#########.#######.#.#######.#######.###  \n" +
            "  #...#.#    F       R I       Z    #.#.#.#  \n" +
            "  #.###.#    D       E C       H    #.#.#.#  \n" +
            "  #.#...#                           #...#.#  \n" +
            "  #.###.#                           #.###.#  \n" +
            "  #.#....OA                       WB..#.#..ZH\n" +
            "  #.###.#                           #.#.#.#  \n" +
            "CJ......#                           #.....#  \n" +
            "  #######                           #######  \n" +
            "  #.#....CK                         #......IC\n" +
            "  #.###.#                           #.###.#  \n" +
            "  #.....#                           #...#.#  \n" +
            "  ###.###                           #.#.#.#  \n" +
            "XF....#.#                         RF..#.#.#  \n" +
            "  #####.#                           #######  \n" +
            "  #......CJ                       NM..#...#  \n" +
            "  ###.#.#                           #.###.#  \n" +
            "RE....#.#                           #......RF\n" +
            "  ###.###        X   X       L      #.#.#.#  \n" +
            "  #.....#        F   Q       P      #.#.#.#  \n" +
            "  ###.###########.###.#######.#########.###  \n" +
            "  #.....#...#.....#.......#...#.....#.#...#  \n" +
            "  #####.#.###.#######.#######.###.###.#.#.#  \n" +
            "  #.......#.......#.#.#.#.#...#...#...#.#.#  \n" +
            "  #####.###.#####.#.#.#.#.###.###.#.###.###  \n" +
            "  #.......#.....#.#...#...............#...#  \n" +
            "  #############.#.#.###.###################  \n" +
            "               A O F   N                     \n" +
            "               A A D   M                     "

    val day20 = Day20()

    @Test
    fun part1() {
        assertEquals(23, day20.part1(input1String.split("\n").map { it.toCharArray() }))
        assertEquals(58, day20.part1(input2String.split("\n").map { it.toCharArray() }))
    }

    @Test
    fun part2() {
        assertEquals(396, day20.part2(input3String.split("\n").map { it.toCharArray() }))
        assertEquals(26, day20.part2(input1String.split("\n").map { it.toCharArray() }))
    }
}