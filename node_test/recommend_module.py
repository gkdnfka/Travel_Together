import json
import sys
import numpy as np
import pandas as pd

userdata = json.loads(sys.argv[1])
postdata = json.loads(sys.argv[2])

label_leng = 29

post_df = pd.DataFrame(postdata)

temp = np.zeros(label_leng)
if post_df.loc[0]["LABELS"] is not None:
    for ch in post_df.loc[0]["LABELS"].split(',') :
        temp[int(ch)-1] = 1

num_by_label = pd.DataFrame({post_df.loc[0]["NOTICEBOARD_NUM"] : temp})

for i in range(1, int(post_df.size/2)):
    temp = np.zeros(label_leng)
    if post_df.loc[i]["LABELS"] is not None:
        for ch in post_df.loc[i]["LABELS"].split(',') :
            temp[int(ch)-1] = 1
    temp_df = pd.DataFrame({post_df.loc[i]["NOTICEBOARD_NUM"] : temp})

    num_by_label = pd.concat([num_by_label, temp_df], axis=1)


user_taste_row = np.zeros(29)
print(userdata[0]['USER_TASTE'])
user_taste = userdata[0]['USER_TASTE'].split(',')
user_taste = [int(taste) for taste in user_taste]

for i in user_taste:
    user_taste_row[i] = 1

user_taste_df = pd.DataFrame({userdata[0]['USER_CODE'] : user_taste_row})
user_taste_df = user_taste_df.T

ret = user_taste_df.dot(num_by_label) / len(user_taste)
print(ret.to_json(orient='split'))