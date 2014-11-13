	addi $sp, $0, 0
	
	addi $a0, $0, 30
	addi $a1, $0, 100
	addi $a2, $0, 20
	jal circle 						# Circle(30,100,20) #head
		
	addi $a0, $0, 30
	addi $a1, $0, 80
	addi $a2, $0, 30
	addi $a3, $0, 30
	jal line							# Line(30,80,30,30) #body
		
	addi $a0, $0, 20
	addi $a1, $0, 1
	addi $a2, $0, 30
	addi $a3, $0, 30
	jal line 						# Line(20,1,30,30) #left leg
		
	addi $a0, $0, 40
	addi $a1, $0, 1
	addi $a2, $0, 30
	addi $a3, $0, 30
	jal line 						# Line(40,1,30,30) #right leg
		
	addi $a0, $0, 15
	addi $a1, $0, 60
	addi $a2, $0, 30
	addi $a3, $0, 50
	jal line 						# Line(15,60,30,50) #left arm
		
	addi $a0, $0, 30
	addi $a1, $0, 50
	addi $a2, $0, 45
	addi $a3, $0, 60
	jal line 						# Line(30,50,45,60) #right arm
		
	addi $a0, $0, 24
	addi $a1, $0, 105
	addi $a2, $0, 3
	jal circle 						# Circle(24,105,3) #left eye
		
	addi $a0, $0, 36
	addi $a1, $0, 105
	addi $a2, $0, 3
	jal circle 						# Circle(36,105,3) #right eye
		
	addi $a0, $0, 25
	addi $a1, $0, 90
	addi $a2, $0, 35
	addi $a3, $0, 90
	jal line 						# Line(25,90,35,90) #mouth center
		
	addi $a0, $0, 25
	addi $a1, $0, 90
	addi $a2, $0, 20
	addi $a3, $0, 95
	jal line 						# Line(25,90,20,95) #mouth left
		
	addi $a0, $0, 35
	addi $a1, $0, 90
	addi $a2, $0, 40
	addi $a3, $0, 95
	jal line 						# Line(35,90,40,95) #mouth right
	
	j end


circle:
# a0 = xc, a1 = yc, a2 = r
	add $s0, $0,  $0 				# s0 = x = 0
	
	add $s1, $a2, $0 				# s1 = y = r
	
	add $t0, $a2, $a2
	addi $t1, $0, 3
	sub $s2, $t1, $t0 			#s2 = g = 3 - 2*r
	
	add $t0, $t0, $t0
	addi $t1, $0, 10
	sub $s3, $t1, $t0				# s3 = diagonalInc = 10 - 4*r
	
	addi $s4, $0, 6				# s4 = rightInc = 6
	
circleLoop:
	addi $t0, $s0, -1
	slt $t0, $t0, $s1
	beq $t0, $0, circleLoopEnd	# while (x <= y) {
	add $t0, $a0, $s0
	add $t1, $a1, $s1
	sw  $t0, 0($sp)
	sw  $t1, 1($sp)
	addi $sp, $sp, 2				# plot (xc+x, yc+y)  
					
	add $t0, $a0, $s0
	sub $t1, $a1, $s1
	sw  $t0, 0($sp)
	sw  $t1, 1($sp)
	addi $sp, $sp, 2				# plot (xc+x, yc-y)
					
	sub $t0, $a0, $s0
	add $t1, $a1, $s1
	sw  $t0, 0($sp)
	sw  $t1, 1($sp)
	addi $sp, $sp, 2				# plot (xc-x, yc+y)
					
	sub $t0, $a0, $s0
	sub $t1, $a1, $s1
	sw  $t0, 0($sp)
	sw  $t1, 1($sp)
	addi $sp, $sp, 2				# plot (xc-x, yc-y)
					
	add $t0, $a0, $s1
	add $t1, $a1, $s0
	sw  $t0, 0($sp)
	sw  $t1, 1($sp)
	addi $sp, $sp, 2				# plot (xc+y, yc+x)
					
	add $t0, $a0, $s1
	sub $t1, $a1, $s0
	sw  $t0, 0($sp)
	sw  $t1, 1($sp)
	addi $sp, $sp, 2				# plot (xc+y, yc-x)
					
	sub $t0, $a0, $s1
	add $t1, $a1, $s0
	sw  $t0, 0($sp)
	sw  $t1, 1($sp)
	addi $sp, $sp, 2				# plot (xc-y, yc+x)
					
	sub $t0, $a0, $s1
	sub $t1, $a1, $s0
	sw  $t0, 0($sp)
	sw  $t1, 1($sp)
	addi $sp, $sp, 2				# plot (xc-y, yc-x) 
					
	slt $t0, $s2, $0 
	bne $t0, $0, circleElse 	# if (g >= 0) {
	add $s2, $s2, $s3				# g += diagonalInc
	addi $s3, $s3, 8				# diagonalInc += 8
	addi $s1, $s1, -1				# y -= 1
	j ifEnd
circleElse:							# else
	add $s2, $s2, $s4				# g += rightInc
	addi $s3, $s3, 4				# diagonalInc += 4
ifEnd:
	addi $s4, $s4, 4				# rightInc += 4
	addi $s0, $s0, 1				# x += 1
	
	j circleLoop
circleLoopEnd:
circleEnd:
	jr $ra

line: 
# store args
	addi $t0, $a0, 0 				#t0 = x0
	addi $t1, $a1, 0 				#t1 = y0
	addi $t2, $a2, 0				#t2 = x1
	addi $t3, $a3, 0 				#t3 = y1

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
skip2: 								#if st == 1 { swap(x0, y0); swap(x1, y1);}
	slt $s0, $t5, $t4 			#s0 = st
	sw  $s0, 0($sp)			   #st stored at mem[0]

	beq $s0, $0, skip3
	add $t0, $t1, $0
	add $t1, $a0, $0
	add $t2, $t3, $0
	add $t3, $a2, $0
skip3:							 #if x0 > x1 { swap(x0, x1); swap (y0, y1); }
	slt $t4, $t2, $t0
	beq $t4, $0, skip4
	add $t4, $t2, $0
	add $t5, $t3, $0
	add $t2, $t0, $0
	add $t3, $t1, $0
	add $t0, $t4, $0
	add $t1, $t5, $0
skip4: 								#put args back in $a0 through $a3
	addi $a0, $t0, 0 #x0
	addi $a1, $t1, 0 #y0
	addi $a2, $t2, 0 #x1
	addi $a3, $t3, 0 #y1
	
	sub $t0, $a2, $a0 			# t0 = deltax = x1 - x0
	sub $t1, $a3, $a1 
	slt $t2, $t1, $0
	beq $t2, $0, skip5
	sub $t1, $0, $t1  			# t1 = deltay = abs(y1 - y0)
skip5:
	add $t2, $0, $0   			# t2 = error = 0
	add $t3, $a1, $0  			# t3 = y = y0
	
										# if (y0 < y1) { ystep = 1 } else { ystep = -1 }
	slt $t4, $a1, $a3
	bne $t4, $0, skip6
	addi $t4, $0, -1
skip6:
	lw $t5, 0($sp)             # t5 = st
	addi $a2, $a2, 1

lineLoop:
	beq $a0, $a2, lineEnd
										# if (st == 1) { plot(y, x) } else { plot(x, y)}
	beq $t5, $0, plotXY
	sw  $t3, 0($sp)
	sw  $a0, 1($sp)
	j skip7
plotXY:
	sw  $a0, 0($sp)
	sw  $t3, 1($sp)
skip7:
	add  $t2, $t2, $t1			# error = error + deltay
	
	# if (2*error >= deltax) { y = y + ystep; error = error - deltax; }
	add $t6, $t2, $t2
	slt $t7, $t6, $t0
	bne $t7, $0, skip8
	add $t3, $t3, $t4
	sub $t2, $t2, $t0
skip8:
	addi $sp, $sp, 2
	addi $a0, $a0, 1
	j lineLoop
lineEnd:
	jr $ra
	
end:
