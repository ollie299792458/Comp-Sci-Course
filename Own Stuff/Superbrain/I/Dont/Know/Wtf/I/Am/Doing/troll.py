print('trololololololololololol');

str = '';
for i in range(1, 47):
	str = '';
	for j in range(0, i):
		str += '1';
	print(str);

for i in range(47, 1):
	for j in range(i, 0):
		str = str[0:i]
	print(str);
