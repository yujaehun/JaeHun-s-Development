import json
import numpy as np
import re
import random
import math
from matplotlib import pyplot as plt

test_review = []
train_number = [50, 100, 200, 400, 800, 1600, 3200, 6400, 12800, 25600]
bag_dic = {}
total_line = 0

num_funny_line = 0
funny_probability = {}
num_not_funny_line = 0
not_funny_probability = {}

num_useful_line = 0
useful_probability = {}
num_not_useful_line = 0
not_useful_probability = {}

num_cool_line = 0
cool_probability = {}
num_not_cool_line = 0
not_cool_probability = {}

num_pos_line = 0
pos_probability = {}
num_not_pos_line = 0
not_pos_probability = {}

baseline_funny = 0
baseline_useful = 0
baseline_cool = 0
baseline_pos = 0

isFunny = []
isUseful = []
isCool = []
isPositive = []
x_values = []

baseFunny = []
baseUseful = []
baseCool = []
basePositive = []

def load(path):
    review = []
    for line in open(path, 'r'):
        row = json.loads(line)
        sum = 0
        sum += row['votes']['funny'] + row['votes']['useful'] + row['votes']['cool']
        if sum >= 3 and sum <= 10:  # 1 - (a): choose the review with 3-10 votes
            review.append(row)

    return np.array(review)

def preprocessing(sentence):
    letters = re.sub('[^a-zA-Z]', ' ', sentence)
    words_list = letters.lower().split()

    return words_list

def training(train_data):
    global bag_dic
    global total_line

    global num_funny_line, num_not_funny_line
    global funny_probability
    global not_funny_probability

    global num_useful_line, num_not_useful_line
    global useful_probability
    global not_useful_probability

    global num_cool_line, num_not_cool_line
    global cool_probability
    global not_cool_probability

    global num_pos_line, num_not_pos_line
    global pos_probability
    global not_pos_probability

    global baseline_funny, baseline_useful, baseline_cool, baseline_pos

    total_word = []
    not_overlapping_word = []

    funny_words = {}
    funny_probability = {}
    not_funny_words = {}
    not_funny_probability = {}
    num_funny_words = 0
    num_not_funny_words = 0

    useful_words = {}
    useful_probability = {}
    not_useful_words = {}
    not_useful_probability = {}
    num_useful_words = 0
    num_not_useful_words = 0

    cool_words = {}
    cool_probability = {}
    not_cool_words = {}
    not_cool_probability = {}
    num_cool_words = 0
    num_not_cool_words = 0

    pos_words = {}
    pos_probability = {}
    not_pos_words = {}
    not_pos_probability = {}
    num_pos_words = 0
    num_not_pos_words = 0

    num_funny_line = 0
    num_useful_line = 0
    num_cool_line = 0
    num_pos_line = 0
    total_line = 0

    baseline_funny = 0
    baseline_useful = 0
    baseline_cool = 0
    baseline_pos = 0

    for rev in train_data:
        sentence = ""
        sentence = rev['text']
        total_line += 1
        if rev['votes']['funny'] > 0:
            num_funny_line += 1
        if rev['votes']['useful'] > 0:
            num_useful_line += 1
        if rev['votes']['cool'] > 0:
            num_cool_line += 1
        if rev['stars'] > 3.5:
            num_pos_line += 1

        words = preprocessing(sentence)

        funny_list = {}
        not_funny_list = {}

        useful_list = {}
        not_useful_list = {}

        cool_list = {}
        not_cool_list = {}
        
        pos_list = {}
        not_pos_list = {}

        for word in words:
            # 1 - (c)
            # Each word that appears in the training review is separated
            # according to labels when it is included in the bag.
            if word in bag_dic:
                total_word.append(word)
                if rev['votes']['funny'] > 0:
                    if word not in funny_list:
                        num_funny_words += 1
                        funny_list[word] = 1
                        if word in funny_words:
                            funny_words[word] += 1
                        else:
                            funny_words[word] = 1
                else:
                    if word not in not_funny_list:
                        num_not_funny_words += 1
                        not_funny_list[word] = 1
                        if word in not_funny_words:
                            not_funny_words[word] += 1
                        else:
                            not_funny_words[word] = 1

                if rev['votes']['useful'] > 0:
                    if word not in useful_list:
                        num_useful_words += 1
                        useful_list[word] = 1
                        if word in useful_words:
                            useful_words[word] += 1
                        else:
                            useful_words[word] = 1
                else:
                    if word not in not_useful_list:
                        num_not_useful_words += 1
                        not_useful_list[word] = 1
                        if word in not_useful_words:
                            not_useful_words[word] += 1
                        else:
                            not_useful_words[word] = 1

                if rev['votes']['cool'] > 0:
                    if word not in cool_list:
                        num_cool_words += 1
                        cool_list[word] = 1
                        if word in cool_words:
                            cool_words[word] += 1
                        else:
                            cool_words[word] = 1
                else:
                    if word not in not_cool_list:
                        num_not_cool_words += 1
                        not_cool_list[word] = 1
                        if word in not_cool_words:
                            not_cool_words[word] += 1
                        else:
                            not_cool_words[word] = 1
                
                if rev['stars'] > 3.5:
                    if word not in pos_list:
                        num_pos_words += 1
                        pos_list[word] = 1
                        if word in pos_words:
                            pos_words[word] += 1
                        else:
                            pos_words[word] = 1
                else:
                    if word not in not_pos_list:
                        num_not_pos_words += 1
                        not_pos_list[word] = 1
                        if word in not_pos_words:
                            not_pos_words[word] += 1
                        else:
                            not_pos_words[word] = 1

    overlapping_set = set(total_word)
    not_overlapping_word = list(overlapping_set)
    num_not_overlapping = len(not_overlapping_word)

    num_not_funny_line = total_line - num_funny_line
    num_not_useful_line = total_line - num_useful_line
    num_not_cool_line = total_line - num_cool_line
    num_not_pos_line = total_line - num_pos_line

    if num_funny_words >= num_not_funny_words:
        baseline_funny = 1
    if num_useful_words >= num_not_useful_words:
        baseline_useful = 1
    if num_cool_words >= num_not_cool_words:
        baseline_cool = 1
    if num_pos_words >= num_not_pos_words:
        baseline_pos = 1

    # 2 - (c):
    # Find the probability that the word will enter each category.
    # The probability of words for each label is
    # (the number of reviews in which the word appeared) + 1 /
    # (the sum of the numbers in which each word appeared in that label) + (the number of words in the bag in the training review).
    # This is applied with Laplace smoothing.
    # Log10 was used for very small probability.
    for word in not_overlapping_word:
        if word in funny_words:
            funny_probability[word] = math.log10(float((funny_words[word] + 1) / (num_funny_words + num_not_overlapping)))
        else:
            funny_probability[word] = math.log10(float((1) / (num_funny_words + num_not_overlapping)))
        if word in not_funny_words:
            not_funny_probability[word] = math.log10(float((not_funny_words[word] + 1) / (num_not_funny_words + num_not_overlapping)))
        else:
            not_funny_probability[word] = math.log10(float((1) / (num_not_funny_words + num_not_overlapping)))
        
        if word in useful_words:
            useful_probability[word] = math.log10(float((useful_words[word] + 1) / (num_useful_words + num_not_overlapping)))
        else:
            useful_probability[word] = math.log10(float((1) / (num_useful_words + num_not_overlapping)))
        if word in not_useful_words:
            not_useful_probability[word] = math.log10(float((not_useful_words[word] + 1) / (num_not_useful_words + num_not_overlapping)))
        else:
            not_useful_probability[word] = math.log10(float((1) / (num_not_useful_words + num_not_overlapping)))
        
        if word in cool_words:
            cool_probability[word] = math.log10(float((cool_words[word] + 1) / (num_cool_words + num_not_overlapping)))
        else:
            cool_probability[word] = math.log10(float((1) / (num_cool_words + num_not_overlapping)))
        if word in not_cool_words:
            not_cool_probability[word] = math.log10(float((not_cool_words[word] + 1) / (num_not_cool_words + num_not_overlapping)))
        else:
            not_cool_probability[word] = math.log10(float((1) / (num_not_cool_words + num_not_overlapping)))
        
        if word in pos_words:
            pos_probability[word] = math.log10(float((pos_words[word] + 1) / (num_pos_words + num_not_overlapping)))
        else:
            pos_probability[word] = math.log10(float((1) / (num_pos_words + num_not_overlapping)))
        if word in not_pos_words:
            not_pos_probability[word] = math.log10(float((not_pos_words[word] + 1) / (num_not_pos_words + num_not_overlapping)))
        else:
            not_pos_probability[word] = math.log10(float((1) / (num_not_pos_words + num_not_overlapping)))
                
    return


# 2 - (d):
# (1) When the trained word is found in each test review,
#     the probability corresponding to the label is obtained by
#     adding(because it has changed to a log value) the probability obtained
#     from the training according to the label.
# (2) After adding the probability of the words,
#     Add log10(the number of times each label appears) / (the number of trainings)
# (3) Predict the higher of each label probability or the non-label probability.
# (4) If the prediction is wrong, increase the loss value by 1.
def testing():
    global funny_probability
    global not_funny_probability
    global useful_probability
    global not_useful_probability
    global cool_probability
    global not_cool_probability
    global pos_probability
    global not_pos_probability
    global total_line, num_funny_line, num_not_funny_line
    global num_useful_line, num_not_useful_line
    global num_cool_line, num_not_cool_line
    global num_pos_line, num_not_pos_line
    global test_review
   
    global isFunny, isUseful, isCool, isPositive
    global baseline_funny, baseline_useful, baseline_cool, baseline_pos
    global baseFunny, baseUseful, baseCool, basePositive

    funny_loss = 0
    useful_loss = 0
    cool_loss = 0
    pos_loss = 0

    base_funny_loss = 0
    base_useful_loss = 0
    base_cool_loss = 0
    base_pos_loss = 0

    if num_funny_line == 0:
        funny_line_prob = -1000
    else:
        funny_line_prob = math.log10(float(num_funny_line / total_line))
    if num_not_funny_line == 0:
        not_funny_line_prob = -1000
    else:
        not_funny_line_prob = math.log10(float(num_not_funny_line / total_line))
    
    if num_useful_line == 0:
        useful_line_prob = -1000
    else:
        useful_line_prob = math.log10(float(num_useful_line / total_line))
    if num_not_useful_line == 0:
        not_useful_line_prob = -1000
    else:
        not_useful_line_prob = math.log10(float(num_not_useful_line / total_line))
    
    if num_cool_line == 0:
        cool_line_prob = -1000
    else:
        cool_line_prob = math.log10(float(num_cool_line / total_line))
    if num_not_cool_line == 0:
        not_cool_line_prob = -1000
    else:
        not_cool_line_prob = math.log10(float(num_not_cool_line / total_line))
    
    if num_pos_line == 0:
        pos_line_prob = -1000
    else:
        pos_line_prob = math.log10(float(num_pos_line / total_line))
    if num_not_pos_line == 0:
        not_pos_line_prob = -1000
    else:
        not_pos_line_prob = math.log10(float(num_not_pos_line / total_line))

    for rev in test_review:
        sentence = ""
        sentence = rev['text']
        words = preprocessing(sentence)

        funny_prob = 0
        not_funny_prob = 0
        useful_prob = 0
        not_useful_prob = 0
        cool_prob = 0
        not_cool_prob = 0
        pos_prob = 0
        not_pos_prob = 0

        for word in words:
            if word in funny_probability:
                funny_prob += funny_probability[word]
            if word in not_funny_probability:
                not_funny_prob += not_funny_probability[word]
            if word in useful_probability:
                useful_prob += useful_probability[word]
            if word in not_useful_probability:
                not_useful_prob += not_useful_probability[word]
            if word in cool_probability:
                cool_prob += cool_probability[word]
            if word in not_cool_probability:
                not_cool_prob += not_cool_probability[word]
            if word in pos_probability:
                pos_prob += pos_probability[word]
            if word in not_pos_probability:
                not_pos_prob += not_pos_probability[word]

        funny_prob += funny_line_prob
        not_funny_prob += not_funny_line_prob
        useful_prob += useful_line_prob
        not_useful_prob += not_useful_line_prob
        cool_prob += cool_line_prob
        not_cool_prob += not_cool_line_prob
        pos_prob += pos_line_prob
        not_pos_prob += not_pos_line_prob
        
        if funny_prob < not_funny_prob and rev['votes']['funny'] > 0:
            funny_loss += 1
        elif funny_prob > not_funny_prob and rev['votes']['funny'] == 0:
            funny_loss += 1
        if useful_prob < not_useful_prob and rev['votes']['useful'] > 0:
            useful_loss += 1
        elif useful_prob > not_useful_prob and rev['votes']['useful'] == 0:
            useful_loss += 1
        if cool_prob < not_cool_prob and rev['votes']['cool'] > 0:
            cool_loss += 1
        elif cool_prob > not_cool_prob and rev['votes']['cool'] == 0:
            cool_loss += 1
        if pos_prob < not_pos_prob and rev['stars'] > 3.5:
            pos_loss += 1
        elif pos_prob > not_pos_prob and rev['stars'] <= 3.5:
            pos_loss += 1

        if baseline_funny and rev['votes']['funny'] == 0:
            base_funny_loss += 1
        elif not baseline_funny and rev['votes']['funny'] > 0:
            base_funny_loss += 1
        if baseline_useful and rev['votes']['useful'] == 0:
            base_useful_loss += 1
        elif not baseline_useful and rev['votes']['useful'] > 0:
            base_useful_loss += 1
        if baseline_cool and rev['votes']['cool'] == 0:
            base_cool_loss += 1
        elif not baseline_cool and rev['votes']['cool'] > 0:
            base_cool_loss += 1
        if baseline_pos and rev['stars'] <= 3.5:
            base_pos_loss += 1
        elif not baseline_pos and rev['stars'] > 3.5:
            base_pos_loss += 1

    print("Funny zero-one loss: {}".format(funny_loss/8000))
    print("Useful zero-one loss: {}".format(useful_loss/8000))
    print("Cool zero-one loss: {}".format(cool_loss/8000))
    print("Star zero-one loss: {}".format(pos_loss/8000))

    isFunny.append(funny_loss/8000)
    isUseful.append(useful_loss/8000)
    isCool.append(cool_loss/8000)
    isPositive.append(pos_loss/8000)

    baseFunny.append(base_funny_loss/8000)
    baseUseful.append(base_useful_loss/8000)
    baseCool.append(base_cool_loss/8000)
    basePositive.append(base_pos_loss/8000)

    return

if __name__ == "__main__":

    review = load('yelp_academic_dataset_review.json')

    # 1 - (b):
    # (1) Stores the number of appearances of all the words
    #     in the selected review according to the notes.
    # (2) Sort the words in order of the number of appearances
    #     and select the first 5000 words.
    every_word = {}
    for rev in review:
        sentence = ""
        sentence = rev['text']
        words_list = preprocessing(sentence)
        for word in words_list:
            if word in every_word:
                every_word[word] += 1
            else:
                every_word[word] = 1

    bag = sorted(every_word.items(), key = lambda x: x[1], reverse=True)
    bag = bag[0:5000]
    
    bag_dic = {bag[i][0]: bag[i][1] for i in range(5000)}


    # 3 - (a)
    # Randomly select 8,000 reviews for the test.
    random.shuffle(review)
    test_review = review[0:8000]
    review = review[8000:]

    for i in range(10):
        print("Start {} case...".format(train_number[i]))
        # 3 - (a)
        # For training, randomly select a fixed number of reviews.
        random.shuffle(review)
        train_data = review[0:train_number[i]]
        training(train_data)
        testing()

        x_values.append(str(i)+"\n"+"("+str(train_number[i])+")")
        print()
    
    plt.figure(1)
    plt.suptitle('Zero-one loss')
    plt.xlabel('Training')
    plt.ylabel('Loss ratio')
    plt.plot(x_values, isFunny)
    plt.plot(x_values, isUseful)
    plt.plot(x_values, isCool)
    plt.plot(x_values, isPositive)
    plt.legend(['isFunny', 'isUseful', 'isCool', 'isPositive'])

    plt.figure(2)
    plt.suptitle('Default loss')
    plt.xlabel('Training')
    plt.ylabel('Loss ratio')
    plt.plot(x_values, baseFunny)
    plt.plot(x_values, baseUseful)
    plt.plot(x_values, baseCool)
    plt.plot(x_values, basePositive)
    plt.legend(['isFunny', 'isUseful', 'isCool', 'isPositive'])
    plt.show()