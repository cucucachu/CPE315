addi $t0, $0, 5
one: beq $t0, $0, four
two: bne $t0, $0, three
three: addi $t0, $t0, -1
j one
four: addi $t0, $t0, 5
five: bne $t0, $0, six
six: beq $t0, $0, seven
	addi $t0, $t0, -1
	j five
seven: bne $0, $0, eight
eight: bne $0, $0, nine
nine: 
