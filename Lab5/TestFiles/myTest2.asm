addi	$sp, $0, 20
jal main
	
addi $v0, $0, 2020
sw $v0, 0($0)
addi $sp, $sp, -1

j end

fake:
	addi $v0, $v0, 1174
	sw $v0, 0($sp)
	addi $sp, $sp, -1


main:
	addi	$t0, $0, 10
	addi  $t1, $0, 15
	
	#add
	add   $v0, $t1, $t0
	sw $v0, 0($sp)
	addi $sp, $sp, -1
	
	#and
	and   $v0, $t1, $t0
	sw $v0, 0($sp)
	addi $sp, $sp, -1
	
	#or
	or   $v0, $t1, $t0
	sw $v0, 0($sp)
	addi $sp, $sp, -1
	
	#sll
	sll   $v0, $t1, 2
	sw $v0, 0($sp)
	addi $sp, $sp, -1
	
	#sub
	sub   $v0, $t1, $t0
	sw $v0, 0($sp)
	addi $sp, $sp, -1
	
	#slt
	addi $t0, $0, 10
	addi $t1, $0, 15 
	slt  $v0, $t0, $t1
	sw $v0, 0($sp)
	addi $sp, $sp, -1
	
	#slt
	slt  $v0, $t1, $t0
	sw $v0, 0($sp)
	addi $sp, $sp, -1
	
	#lw
	lw $v0, 2($sp)
	sw $v0, 0($sp)
	addi $sp, $sp, -1
	
	beq $t0, $t1, fake
	bne $t0, $t1, testEnd
	addi $v0, $0, 1174
	sw $v0, 0($sp)
	addi $sp, $sp, -1
	

testEnd:	
	jr $ra

end:stupidlabel:
