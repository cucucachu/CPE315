start: 
	addi $v0, $0, 1
	addi $sp, $0, 100
	addi $s0, $0, 0
	addi $t0, $0, 100
loop:
	beq $t0, $0, end
	add $a0, $v0, $0
	jal double
	addi $sp, $sp, -1
	sw $v0, 0($sp)
	addi $t0, $t0, -1
	j loop
	
	
	
	
double:
	add $v0, $a0, $a0
	jr $ra

end: 
