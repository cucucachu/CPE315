# Parity Program
# Author: Cody Jones
# CPE 315
#
# c code:
#
#int parity(int a) {
#  int numOnes;
#  int i;
#	numOnes = 0;
#	for (i = 0; i < 32; i++) {
#     if (a & 1 == 1)
#        numOnes++;
#     a = a >> 1;
#	}
#  return (numOnes & 1); 
#}

.globl welcome
.globl prompt
.globl result

.data

welcome:
	.asciiz " This program finds the parity of a number. \n\n"

prompt:
	.asciiz " Enter an integer: "

result: 
	.asciiz " \n Parity = "

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
	ori     $a0, $a0, 0x2F
	syscall

	# Read 1st integer from the user (5 is loaded into $v0, then a syscall)
	ori     $v0, $0, 5
	syscall

	# Clear $s0 for the integer
	ori     $s0, $0, 0	

	# Save integer in $s0
	addu    $s0, $v0, $0

	# Display the result text
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0,0x45
	syscall
	
	#calculate the parity of the integer in $s0
   add   $t0, $0, $0 #t0 is numOnes
   add   $t1, $0, $0 #t1 is i

parity_loop: 
   slti  $t2, $t1, 32
   beq   $t2, $0, loop_end
   andi  $t3, $s0, 1
   beq   $t3, $0, no_add
   addi  $t0, $t0, 1
no_add:
   srl   $s0, $s0, 1
   addi  $t1, $t1, 1
   j parity_loop
   
loop_end:	      
	# Display 1 if parity is even, 0 otherwise
	ori   $v0, $0, 1	
	addi  $t0, $t0, 1		
	andi 	$a0, $t0, 1
	syscall
	
	# output newline for cleanliness.
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0,0x52
	syscall
	
	# Exit (load 10 into $v0)
	ori     $v0, $0, 10
	syscall

