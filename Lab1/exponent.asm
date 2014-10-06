# Exponentiation Program
# Author: Cody Jones
# CPE 315
#
# c code:
#
#   int raise(int x, int y) {
#      int i;
#      int answer;
#      
#      answer = x;
#      
#      if (y == 0)
#         return 1;
#     
#      for (i = 1; i < y; i++)
#         answer = multiply(answer, x);
#      
#      return answer;
#   }
#   
#   int multiply(int x, int y) {
#      int i;
#      int product;
#      
#      for (i = 0, product = 0; i < y; i++)
#         product += x;
#      
#      return product;
#   }
#
#


# declare global so programmer can see actual addresses.
.globl welcome
.globl prompt1
.globl prompt2
.globl result

#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
	.asciiz " Raise a number to a power \n\n"

prompt1:
	.asciiz " Enter the base: "

prompt2:
	.asciiz " Enter the exponent: "

result: 
	.asciiz " \n Result = "

#Text Area (i.e. instructions)
.text

j main

multiply:
   add   $v0, $0, $0
   add   $t0, $0, $0
multiplyLoop:
   slt   $t1, $t0, $a1
   beq   $t1, $0, multiplyEnd
   add   $v0, $v0, $a0
   addi  $t0, $t0, 1
   j     multiplyLoop
multiplyEnd:
   jr $ra
   
raise:
   addi  $sp, $sp, -20
   sw    $ra, 0($sp)
   sw    $s0, 4($sp)
   sw    $s1, 8($sp)
   sw    $s2, 12($sp)
   sw    $s3, 16($sp)
   add   $s0, $a0, $0
   add   $s1, $a0, $0
   add   $s2, $a1, $0
   bne   $a1, $0, skip
   addi  $v0, $0, 1
   addi  $sp, $sp, 4
   jr    $ra
skip:
   addi  $s3, $0, 1
raiseLoop:
   slt   $t0, $s3, $s2
   beq   $t0, $0, raiseEnd
   add   $a0, $s0, $0
   add   $a1, $s1, $0
   jal   multiply
   add   $s0, $v0, $0
   addi  $s3, $s3, 1
   j     raiseLoop
raiseEnd:
   add   $v0, $s0, $0
   lw    $ra, 0($sp)
   lw    $s0, 4($sp)
   lw    $s1, 8($sp)
   lw    $s2, 12($sp)
   lw    $s3, 16($sp)
   addi  $sp, $sp, 20
   jr    $ra 
   

main:

	# Display the welcome message (load 4 into $v0 to display)
	ori     $v0, $0, 4			

	# This generates the starting address for the welcome message.
	# (assumes the register first contains 0).
	lui     $a0, 0x1001
	syscall

	# Display prompt1
	ori     $v0, $0, 4
	lui     $a0, 0x1001
	ori     $a0, $a0,0x1E
	syscall

	# Read base from user
	ori     $v0, $0, 5
	syscall
	addu    $s0, $v0, $0
	
	# Display prompt2
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0,0x30
	syscall

	# Read the exponent
	ori	$v0, $0, 5			
	syscall
	addu    $s1, $v0, $0 

	# Display the result text
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0,0x46
	syscall
	
	add   $a0, $s0, $0
	add   $a1, $s1, $0
	jal   raise
	
	add   $s0, $v0, $0
	
	# Display the result
	ori     $v0, $0, 1			
	add 	$a0, $s0, $0
	syscall
	
	# Exit (load 10 into $v0)
	ori     $v0, $0, 10
	syscall

