.macro  DEBUG_PRINT     reg
	csrw 0x7b2, \reg
.endm

.text
div:
	addi sp,sp,-32	# Allocate stack space
	
	# store any callee-saved register you might overwrite
	sw ra, 0(sp)
	
	# temp register list
	# t0, quotient
	# t1, remainder
	# t2, 0 to 32 iterator
	# t3, loop check for iterator (equal to 32)
	# t4, ith bitmask
	# t5, one
	# t6, ith bit of numerator

	# do your work
	# initializer quotient and remainder to zero, and t5 to 1 and t3 to 32
	addi t0, zero, 0
	addi t1, zero, 0	
	addi t5, zero, 1
	addi t3, zero, 32

	# deal with 0 denominator
	bne t0, zero, divnotzero
	# if zero
	ecall
	divnotzero:

	# set up loop
	addi t2, zero, 0

	# iterate
	divloop:
		# left shift R by one bit
		slli t1, t1, 1
		# get i bit mask
		sll t4, t5, t2
		# get bit i of the numerator
		and t6, a0, t4
		# set least significant bit of R to ith bit
		or t1, t1, t6
		divloopif:
			# subtract the denominator from R
			sub t1, t1, a1
			# set Q(i) to 1
			or t0, t0, t4
		# add 1 to loop, and if not 32, loop round to beginning
		addi t2, t2, 1
		bne t2, t3, divloop

	#move result to correct registers
	addi a0, t0, 0
	addi a1, t1, 0

	# load every register you stored above
	lw ra, 0(sp)
	addi sp,sp,32 	# Free up stack space
	ret

.global main		# Export the symbol 'main' so we can call it from other files
.type main, @function
main:
	addi sp,sp,-32 	# Allocate stack space
	sw ra, 0(sp)

	#test template
	addi    a0, zero, 12    # a0 <- 12
        addi    a1, zero, 4     # a1 <- 4
        call    div
        DEBUG_PRINT a0          # display the quotient
        DEBUG_PRINT a1          # display the remainder

        addi    a0, zero, 93    # a0 <- 93
        addi    a1, zero, 7     # a1 <- 7
        call    div
        DEBUG_PRINT a0          # display the quotient
        DEBUG_PRINT a1          # display the remainder

        lui     a0, (0x12345000>>12)
        ori     a0, a0, 0x678   # a0 <- 0x12345678
        addi    a1, zero, 255   # a1 <- 255
        call    div
        DEBUG_PRINT a0          # display the quotient
        DEBUG_PRINT a1          # display the remainder
	#end of test template

	lw ra, 0(sp)
	addi sp,sp,32 	# Free up stack space
	ret
