# Divide Program
# Author: Cody Jones
# CPE 315
#
# c code:
#
#int divide(int upper, int lower, int divisor) {
#   int carry;
#   while(divisor != 1) {
#      carry = upper & 1;
#      carry = carry << 31;
#      upper = upper >> 1;
#      lower = lower >> 1;
#      divisor = divisor >> 1;
#   }
#   printf("Upper: %d", &upper);
#   printf("Lower: %d", &lower);
#}

.globl welcome
.globl prompt
.globl prompt2
.globl prompt3
.globl result
.globl result2

.data

welcome:
	.asciiz " This program divides one number by a power of two.\n\n"

prompt:
	.asciiz " Enter the upper 32 bits of the number: "

prompt2:
	.asciiz " Enter the lower 32 bits of the number: "

prompt3:
	.asciiz " Enter the divisor: "

result: 
	.asciiz " \n Upper result = "

result2: 
	.asciiz " \n Lower result = "

newline:
   .asciiz " \n "

.text

main:

	# Display the welcome message
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	syscall

	# Display prompt
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0, 0x36
	syscall

	# Read upper dividen from the user
	ori     $v0, $0, 5
	syscall


	# Save dividen in $s0
	ori     $s0, $0, 0	
	addu    $s0, $v0, $0
	
	# Display prompt
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0, 0x5F
	syscall

	# Read lower dividen from the user
	ori     $v0, $0, 5
	syscall


	# Save dividen in $s1
	ori     $s1, $0, 0	
	addu    $s1, $v0, $0

	# Display prompt
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0, 0x88
	syscall

	# Read divisor from the user
	ori     $v0, $0, 5
	syscall

	# Save divisor in $s2
	ori     $s2, $0, 0	
	addu    $s2, $v0, $0
	
	add   $t0, $s0, $0
	add   $t1, $s1, $0
	add   $t2, $s2, $0
	addi  $t4, $0, 1

divide_loop: 
   beq   $t2, $t4, loop_end
   add   $t3, $t0, $0
   andi  $t3, $t3, 1
   sll   $t3, $t3, 31
   srl   $t0, $t0, 1
   srl   $t1, $t1, 1
   or    $t1, $t1, $t3
   srl   $t2, $t2, 1
   j divide_loop
   
loop_end:	      

	# Display the upper result text
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0,0x9D
	syscall
	
	# Display upper result
	ori   $v0, $0, 1		
	add 	$a0, $t0, $0
	syscall     

	# Display the lower result text
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0,0xB0
	syscall
	
	# Display lower result
	ori   $v0, $0, 1		
	add 	$a0, $t1, $0
	syscall
	
	# Exit (load 10 into $v0)
	ori     $v0, $0, 10
	syscall

