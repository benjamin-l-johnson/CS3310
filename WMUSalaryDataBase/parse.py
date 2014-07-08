def nameAdd(raw):
	for x in range(40):
			temp = next(raw)
			if "WESTERN MICHIGAN UNIVERSITY" in temp or "POSITION TITLE" in temp:
				break
			else:
				
				print(temp)
				hold = temp.split(",")
				temp = hold[0]+" " + hold[1]
				names.append(temp)
	return temp

def positionTitle(raw):
	for x in range(40):
			temp = next(raw)
			if "WESTERN MICHIGAN UNIVERSITY" in temp or "DEPARTMENT" in temp or "Page No." in temp:
				break
			else:
				print(temp)
				title.append(temp)
	return temp

def departmentAdd(raw):
	for x in range(40):
			temp = next(raw)
			if "Page No." in temp:
				break
			else:
				print(temp)
				dep.append(temp)
	return temp

def payrateAdd(raw):
	for x in range(40):
			temp = next(raw)
			if "APPOINTMENT PERIOD" in temp:
				break
			else:
				print(temp)
				pay.append(temp)
	return temp

def apptPerAdd(raw):
	for x in range(40):
		temp = next(raw)
		if "FTE" in temp:
			break
		else:
			print(temp)
			appt.append(temp)
	return temp

raw = open("wmu.csv", "r+")
names = []
title = []
dep = []
pay = []
appt = []
for line in raw:
	temp = line 
	if "NAME" in temp:
		temp = nameAdd(raw)
	if "POSITION TITLE" in temp:
		temp = positionTitle(raw)
	if "DEPARTMENT" in temp:
		temp = departmentAdd(raw)
	if "PAY RATE" in temp:
		temp = payrateAdd(raw)
	if "APPOINTMENT PERIOD" in temp:
		temp = apptPerAdd(raw)

newtxt = open("lines.txt", "w+")

i=0
for lines in title:
	#newtxt.write(lines)

	try:
		hold = names[i].strip() + "--" + lines.strip() + "--" + dep[i].strip() + "--" + pay[i].strip()+ "--" + appt[i].strip()
		newtxt.write(hold)
		newtxt.write("\n")
		i+=1
	except IndexError:
		break
newtxt.close()
raw.close()

'''
	if "Report ID" in temp:
		pass
		#do nothing
	if "As of Date:" in temp:
		pass
		#do nothing
'''