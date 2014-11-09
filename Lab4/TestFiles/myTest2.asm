lw $s1, 0($s0)
lw $s2, 0($s1)
lw $s3, 0($s2)
lw $s4, 0($s3)
lw $s5, 0($s4)
beq $0, $0, part2

addi $t0, $0, 1111

part2:

spot1: j spot5
spot2: addi $s0, $0, 1
		 j spot4
spot3: addi $s1, $0, 2 
spot4: addi $s2, $0, 3
		 beq $0, $0, end
spot5: bne $0, $0, end
jal spot2
end:

# $t = 0
# $s0 = 1
# $s2 = 3
