code = 'CIN'
code.upper()
multipliedTogether = 1
for letter in code.upper():
	temp = ord(letter)
	multipliedTogether = temp * multipliedTogether
#print(multipliedTogether)
remain = multipliedTogether % 20
homeRRN = remain+1
print('The homeRRN is ',homeRRN)