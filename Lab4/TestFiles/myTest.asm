lw $s0, 0($sp)
addi $t0, $0, 1

sw $t0, 0($sp)
addi $t1, $t1, 1

loop:
beq $t0, $0, end
addi $t0, $t0, -1
j loop
add $0 $0 $0

end: sub $s0 $0 $s0
