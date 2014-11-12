
	addi $a0, $0, 4
	addi $a1, $0, 5
	addi $a2, $0, 2
	addi $a3, $0, 1
	jal line
	j end




line: 
# store args
	addi $t0, $a0, 0 #x0
	addi $t1, $a1, 0 #y0
	addi $t2, $a2, 0 #x1
	addi $t3, $a3, 0 #y1

#abs(y1 - y0) --> $t4
	sub $t4, $t3, $t1
	slt $t5, $t4, $0
	beq $t5, $0, skip1
	sub $t4, $0, $t4 
skip1:
#abs(x1 - x0) --> $t5
	sub $t5, $t2, $t0
	slt $t6, $t5, $0
	beq $t6, $0, skip2
	sub $t5, $0, $t5 
skip2: #if st == 1 { swap(x0, y0); swap(x1, y1);}
	slt $s0, $t5, $t4 #s0 = st

	beq $s0, $0, skip3
	add $t0, $t1, $0
	add $t1, $a0, $0
	add $t2, $t3, $0
	add $t3, $a2, $0
skip3: #if x0 > x1 { swap(x0, x1); swap (y0, y1); }
	slt $t4, $t2, $t0
	beq $t4, $0, skip4
	add $t4, $t2, $0
	add $t5, $t3, $0
	add $t2, $t0, $0
	add $t3, $t1, $0
	add $t0, $t4, $0
	add $t1, $t5, $0
skip4:
	
	
end:
