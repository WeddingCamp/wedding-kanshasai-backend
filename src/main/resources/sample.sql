SET CHARACTER_SET_CLIENT = utf8;
SET CHARACTER_SET_CONNECTION = utf8;

INSERT INTO events (id, name)
VALUES (0x018a0d02dd6f3d6a453cf75894612edc, 'サンプルイベント 1');

INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02dd9cb0d410cb5b424f9a101e,
    0x018a0d02dd6f3d6a453cf75894612edc,
    '1_1 四択クイズ1 (回答: 4)',
    '{}',
    1
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02dda9f70b7419ff10e7248393,
    0x018a0d02dd9cb0d410cb5b424f9a101e,
    '1_1_1 回答1'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02ddb4ab56f13e9009a58481cb,
    0x018a0d02dd9cb0d410cb5b424f9a101e,
    '1_1_2 回答2'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02ddbad22b84a7f466c1bfc30d,
    0x018a0d02dd9cb0d410cb5b424f9a101e,
    '1_1_3 回答3'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02ddbe29968de185b13fd6ce5e,
    0x018a0d02dd9cb0d410cb5b424f9a101e,
    '1_1_4 回答4 [正答]'
);

UPDATE
    quizzes
SET
    correct_answer = '{"choiceIdList": ["01H86G5QDY56B8VRC5P4ZXDKJY"]}'
WHERE
    id = 0x018a0d02dd9cb0d410cb5b424f9a101e;

    INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02ddc32f2d0fba48535d702ad9,
    0x018a0d02dd6f3d6a453cf75894612edc,
    '1_2 四択クイズ2 (回答: 4)',
    '{}',
    1
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02ddc6f4afb38f1edbf399c220,
    0x018a0d02ddc32f2d0fba48535d702ad9,
    '1_2_1 回答1'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02ddca2d88907573f2bbbfc093,
    0x018a0d02ddc32f2d0fba48535d702ad9,
    '1_2_2 回答2'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02ddcf9427b0e6fac34ddf9864,
    0x018a0d02ddc32f2d0fba48535d702ad9,
    '1_2_3 回答3'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02ddd41d4678e73f8e50144dbd,
    0x018a0d02ddc32f2d0fba48535d702ad9,
    '1_2_4 回答4 [正答]'
);

UPDATE
    quizzes
SET
    correct_answer = '{"choiceIdList": ["01H86G5QEM3N37HSSZHS818KDX"]}'
WHERE
    id = 0x018a0d02ddc32f2d0fba48535d702ad9;

    INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02ddd827dbb234f9dba88dc8ef,
    0x018a0d02dd6f3d6a453cf75894612edc,
    '1_3 四択クイズ3 (回答: 1)',
    '{}',
    1
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02ddda10070065045493d29cf0,
    0x018a0d02ddd827dbb234f9dba88dc8ef,
    '1_3_1 回答1 [正答]'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02dddf1da59787c7f6447d4cc6,
    0x018a0d02ddd827dbb234f9dba88dc8ef,
    '1_3_2 回答2'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02dde3844a5ae0291923d01470,
    0x018a0d02ddd827dbb234f9dba88dc8ef,
    '1_3_3 回答3'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02dde8087d6e975330ed31595e,
    0x018a0d02ddd827dbb234f9dba88dc8ef,
    '1_3_4 回答4'
);

UPDATE
    quizzes
SET
    correct_answer = '{"choiceIdList": ["01H86G5QET203G0S84AJ9X577G"]}'
WHERE
    id = 0x018a0d02ddd827dbb234f9dba88dc8ef;

    INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02ddeea987d0ebd6d3d1311888,
    0x018a0d02dd6f3d6a453cf75894612edc,
    '1_4 四択クイズ4 (回答: 4)',
    '{}',
    1
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02ddf1ca303eab6e5014ea5b27,
    0x018a0d02ddeea987d0ebd6d3d1311888,
    '1_4_1 回答1'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02ddf7a6a44561151b36dcd685,
    0x018a0d02ddeea987d0ebd6d3d1311888,
    '1_4_2 回答2'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02ddfda9a57fd3e4d967524ece,
    0x018a0d02ddeea987d0ebd6d3d1311888,
    '1_4_3 回答3'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de02d80060d2561d0d8a4007,
    0x018a0d02ddeea987d0ebd6d3d1311888,
    '1_4_4 回答4 [正答]'
);

UPDATE
    quizzes
SET
    correct_answer = '{"choiceIdList": ["01H86G5QG2V0061MJP3M6RMG07"]}'
WHERE
    id = 0x018a0d02ddeea987d0ebd6d3d1311888;

    INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02de068775533d3f028cfa7bbf,
    0x018a0d02dd6f3d6a453cf75894612edc,
    '1_5 四択クイズ5 (回答: 4)',
    '{}',
    1
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de08489da3eb96c997aab9c9,
    0x018a0d02de068775533d3f028cfa7bbf,
    '1_5_1 回答1'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de0c73daa528094cf0d7ac65,
    0x018a0d02de068775533d3f028cfa7bbf,
    '1_5_2 回答2'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de1069f3b9e9e6ec1755b2c3,
    0x018a0d02de068775533d3f028cfa7bbf,
    '1_5_3 回答3'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de155df08fe9ec34ea8215c5,
    0x018a0d02de068775533d3f028cfa7bbf,
    '1_5_4 回答4 [正答]'
);

UPDATE
    quizzes
SET
    correct_answer = '{"choiceIdList": ["01H86G5QGNBQR8ZTFC6KN845E5"]}'
WHERE
    id = 0x018a0d02de068775533d3f028cfa7bbf;

    INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02de1be5ccc921cfd42fef3394,
    0x018a0d02dd6f3d6a453cf75894612edc,
    '1_6 複数正解四択クイズ1 (回答: [1, 3])',
    '{}',
    1
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de205b519adf2ae11e9e4133,
    0x018a0d02de1be5ccc921cfd42fef3394,
    '1_6_1 回答1 [正答]'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de248a96ea132ad5322b7c20,
    0x018a0d02de1be5ccc921cfd42fef3394,
    '1_6_2 回答2'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de29f6c63e19569094b2f8c8,
    0x018a0d02de1be5ccc921cfd42fef3394,
    '1_6_3 回答3 [正答]'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de2d65f21c5281eeef98b4c4,
    0x018a0d02de1be5ccc921cfd42fef3394,
    '1_6_4 回答4'
);

UPDATE
    quizzes
SET
    correct_answer = '{"choiceIdList": ["01H86G5QH0BD8SNQSAW4F9WG9K", "01H86G5QH9YV33W6APJ2AB5Y68"]}'
WHERE
    id = 0x018a0d02de1be5ccc921cfd42fef3394;

INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02de33559ad790bd62305712ce,
    0x018a0d02dd6f3d6a453cf75894612edc,
    '1_7 全回答不正解四択クイズ1 (回答: [])',
    '{}',
    1
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de38155daf94ef4a36ec8be0,
    0x018a0d02de33559ad790bd62305712ce,
    '1_7_1 回答1'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de3cf9e4671a0843545e6b35,
    0x018a0d02de33559ad790bd62305712ce,
    '1_7_2 回答2'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de40f5fbbb3cac2337cd7ade,
    0x018a0d02de33559ad790bd62305712ce,
    '1_7_3 回答3'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de467ed3067c8e1e013b6f8b,
    0x018a0d02de33559ad790bd62305712ce,
    '1_7_4 回答4'
);

UPDATE
    quizzes
SET
    correct_answer = '{"choiceIdList": [""]}'
WHERE
    id = 0x018a0d02de33559ad790bd62305712ce;

INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02de4b5d0bca4402ef0607245a,
    0x018a0d02dd6f3d6a453cf75894612edc,
    '1_8 全回答正解四択クイズ (回答: [1, 2, 3, 4])',
    '{}',
    1
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de4d4022c5808d3bff789515,
    0x018a0d02de4b5d0bca4402ef0607245a,
    '1_8_1 回答1 [正答]'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de5271a8a197c8e04e75c79c,
    0x018a0d02de4b5d0bca4402ef0607245a,
    '1_8_2 回答2 [正答]'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de57237e9e80923c525a8d7b,
    0x018a0d02de4b5d0bca4402ef0607245a,
    '1_8_3 回答3 [正答]'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de5f1dceb050da275debb0fe,
    0x018a0d02de4b5d0bca4402ef0607245a,
    '1_8_4 回答4 [正答]'
);

UPDATE
    quizzes
SET
    correct_answer = '{"choiceIdList": ["01H86G5QJD80HCB04D7FZQH58N", "01H86G5QJJE6MA35Y8W177BHWW", "01H86G5QJQ4DZ9X04J7H95N3BV", "01H86G5QJZ3Q7B0M6T4XEYQC7Y"]}'
WHERE
    id = 0x018a0d02de4b5d0bca4402ef0607245a;

INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02de653a1ab4a6f50e30532807,
    0x018a0d02dd6f3d6a453cf75894612edc,
    '1_9 回答未定問題 (回答: 未定)',
    '{}',
    3
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de691b2f8c6f0768494fa339,
    0x018a0d02de653a1ab4a6f50e30532807,
    '1_9_1 回答1'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de6f1bbb8a5bbf083c0b165a,
    0x018a0d02de653a1ab4a6f50e30532807,
    '1_9_2 回答2'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de74d18322d0a4bd8cc6f213,
    0x018a0d02de653a1ab4a6f50e30532807,
    '1_9_3 回答3'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de79812dcd4ad4b011026ef4,
    0x018a0d02de653a1ab4a6f50e30532807,
    '1_9_4 回答4'
);

INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02de7fd3ec1b51f7f9ba702a7b,
    0x018a0d02dd6f3d6a453cf75894612edc,
    '1_10 並び替えクイズ (回答: 3 -> 1 -> 2 -> 4)',
    '{}',
    2
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de8170c1feae03686760fe39,
    0x018a0d02de7fd3ec1b51f7f9ba702a7b,
    '1_10_1 回答1'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de85e8425b32306df75585fd,
    0x018a0d02de7fd3ec1b51f7f9ba702a7b,
    '1_10_2 回答2'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de89e76c8c215da6a319ce5d,
    0x018a0d02de7fd3ec1b51f7f9ba702a7b,
    '1_10_3 回答3'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de8de78ceea194d6f6baffaf,
    0x018a0d02de7fd3ec1b51f7f9ba702a7b,
    '1_10_4 回答4'
);

UPDATE
    quizzes
SET
    correct_answer = '{"choiceIdList": ["01H86G5QM9WXP8R8AXMTHHKKJX", "01H86G5QM1E30ZXBG3D1KP1ZHS", "01H86G5QM5X115PCHGDQVNB1FX", "01H86G5QMDWY6EX8CMTVVBNZXF"]}'
WHERE
    id = 0x018a0d02de7fd3ec1b51f7f9ba702a7b;

INSERT INTO events (id, name)
VALUES (0x018a0d02de92199d3e9707431342954e, 'サンプルイベント 2');

INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02de952f1623e48b8380b59935,
    0x018a0d02de92199d3e9707431342954e,
    '2_1 四択クイズ1 (回答: 2)',
    '{}',
    1
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de9810403a0a6d9a1d8a9431,
    0x018a0d02de952f1623e48b8380b59935,
    '2_1_1 回答1'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02de9ceff08109c5ea5e52b1cc,
    0x018a0d02de952f1623e48b8380b59935,
    '2_1_2 回答2 [正答]'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02dea3dedf9842dc608625bab5,
    0x018a0d02de952f1623e48b8380b59935,
    '2_1_3 回答3'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02dea8e755c3eb2eef7219e9e5,
    0x018a0d02de952f1623e48b8380b59935,
    '2_1_4 回答4'
);

UPDATE
    quizzes
SET
    correct_answer = '{"choiceIdList": ["01H86G5QMWXZR822E5X9F55CEC"]}'
WHERE
    id = 0x018a0d02de952f1623e48b8380b59935;

    INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02deadbb80b01fa5e2c69738da,
    0x018a0d02de92199d3e9707431342954e,
    '2_2 四択クイズ2 (回答: 1)',
    '{}',
    1
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02deaf614b4323a247a676cfaa,
    0x018a0d02deadbb80b01fa5e2c69738da,
    '2_2_1 回答1 [正答]'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02deb59c8abe268d209bc0a90e,
    0x018a0d02deadbb80b01fa5e2c69738da,
    '2_2_2 回答2'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02deba8ad41a566c9b41131fab,
    0x018a0d02deadbb80b01fa5e2c69738da,
    '2_2_3 回答3'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02debf965e6a75fdf6cadc8526,
    0x018a0d02deadbb80b01fa5e2c69738da,
    '2_2_4 回答4'
);

UPDATE
    quizzes
SET
    correct_answer = '{"choiceIdList": ["01H86G5QNFC55M68X28YK7DKXA"]}'
WHERE
    id = 0x018a0d02deadbb80b01fa5e2c69738da;

    INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02dec60422b01db1d5c2f44962,
    0x018a0d02de92199d3e9707431342954e,
    '2_3 四択クイズ3 (回答: 2)',
    '{}',
    1
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02dec99a474255bdcfe272d965,
    0x018a0d02dec60422b01db1d5c2f44962,
    '2_3_1 回答1'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02ded08aab6ededf3ed667411f,
    0x018a0d02dec60422b01db1d5c2f44962,
    '2_3_2 回答2 [正答]'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02ded454e8dd7460d7f553f0f4,
    0x018a0d02dec60422b01db1d5c2f44962,
    '2_3_3 回答3'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02ded8d69cf363e132a25b8223,
    0x018a0d02dec60422b01db1d5c2f44962,
    '2_3_4 回答4'
);

UPDATE
    quizzes
SET
    correct_answer = '{"choiceIdList": ["01H86G5QPGHANPXQPZ7VB6EG8Z"]}'
WHERE
    id = 0x018a0d02dec60422b01db1d5c2f44962;

    INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02dedd73a4ad03e619016715ba,
    0x018a0d02de92199d3e9707431342954e,
    '2_4 四択クイズ4 (回答: 2)',
    '{}',
    1
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02dee0cab996ff739614aa2f4a,
    0x018a0d02dedd73a4ad03e619016715ba,
    '2_4_1 回答1'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02dee50f407052fee20d4602e7,
    0x018a0d02dedd73a4ad03e619016715ba,
    '2_4_2 回答2 [正答]'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02deea777fe0365819655d4e0a,
    0x018a0d02dedd73a4ad03e619016715ba,
    '2_4_3 回答3'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02def09136038d30097aed3490,
    0x018a0d02dedd73a4ad03e619016715ba,
    '2_4_4 回答4'
);

UPDATE
    quizzes
SET
    correct_answer = '{"choiceIdList": ["01H86G5QQ51X070MQYW86MC0Q7"]}'
WHERE
    id = 0x018a0d02dedd73a4ad03e619016715ba;

    INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02def4887bcd1abbf7bc222fcd,
    0x018a0d02de92199d3e9707431342954e,
    '2_5 四択クイズ5 (回答: 1)',
    '{}',
    1
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02def651fcb1b2974fa1b087ad,
    0x018a0d02def4887bcd1abbf7bc222fcd,
    '2_5_1 回答1 [正答]'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02defb8341987c7e975b5dcd3e,
    0x018a0d02def4887bcd1abbf7bc222fcd,
    '2_5_2 回答2'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df00d5ee94c6d6d393590a22,
    0x018a0d02def4887bcd1abbf7bc222fcd,
    '2_5_3 回答3'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df04fa4956d0317dc347cd61,
    0x018a0d02def4887bcd1abbf7bc222fcd,
    '2_5_4 回答4'
);

UPDATE
    quizzes
SET
    correct_answer = '{"choiceIdList": ["01H86G5QQPA7YB3CMQ9YGV11XD"]}'
WHERE
    id = 0x018a0d02def4887bcd1abbf7bc222fcd;

    INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02df0982ecb68aec4ad7dbf36d,
    0x018a0d02de92199d3e9707431342954e,
    '2_6 複数正解四択クイズ1 (回答: [1, 2])',
    '{}',
    1
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df0be5c0243a2a950b217450,
    0x018a0d02df0982ecb68aec4ad7dbf36d,
    '2_6_1 回答1 [正答]'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df1196659e5fd057eb80cb12,
    0x018a0d02df0982ecb68aec4ad7dbf36d,
    '2_6_2 回答2 [正答]'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df158d7ed452ab1ee19d22b9,
    0x018a0d02df0982ecb68aec4ad7dbf36d,
    '2_6_3 回答3'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df194c667e9e6a13281abdb4,
    0x018a0d02df0982ecb68aec4ad7dbf36d,
    '2_6_4 回答4'
);

UPDATE
    quizzes
SET
    correct_answer = '{"choiceIdList": ["01H86G5QRBWQ028EHAJM5J2X2G", "01H86G5QRHJSJSWQYGAZNR1JRJ"]}'
WHERE
    id = 0x018a0d02df0982ecb68aec4ad7dbf36d;

INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02df1ebc092b0875fffe99460b,
    0x018a0d02de92199d3e9707431342954e,
    '2_7 全回答不正解四択クイズ1 (回答: [])',
    '{}',
    1
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df21d2bb449fa97e96788b99,
    0x018a0d02df1ebc092b0875fffe99460b,
    '2_7_1 回答1'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df257726bdfed397a3851a3d,
    0x018a0d02df1ebc092b0875fffe99460b,
    '2_7_2 回答2'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df2a501a0129a18624a0f970,
    0x018a0d02df1ebc092b0875fffe99460b,
    '2_7_3 回答3'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df30b050b95c1012555868a5,
    0x018a0d02df1ebc092b0875fffe99460b,
    '2_7_4 回答4'
);

UPDATE
    quizzes
SET
    correct_answer = '{"choiceIdList": [""]}'
WHERE
    id = 0x018a0d02df1ebc092b0875fffe99460b;

INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02df363138b01fdef1794c16a6,
    0x018a0d02de92199d3e9707431342954e,
    '2_8 全回答正解四択クイズ (回答: [1, 2, 3, 4])',
    '{}',
    1
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df386024272719c8e6460e70,
    0x018a0d02df363138b01fdef1794c16a6,
    '2_8_1 回答1 [正答]'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df3ee91563957dd8ad287c5f,
    0x018a0d02df363138b01fdef1794c16a6,
    '2_8_2 回答2 [正答]'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df426eaed10210925dff6729,
    0x018a0d02df363138b01fdef1794c16a6,
    '2_8_3 回答3 [正答]'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df4735d1a0b7bf9915663d8e,
    0x018a0d02df363138b01fdef1794c16a6,
    '2_8_4 回答4 [正答]'
);

UPDATE
    quizzes
SET
    correct_answer = '{"choiceIdList": ["01H86G5QSRC0J2E9RSS3K4C3KG", "01H86G5QSYX4AP75BXV2PJGZ2Z", "01H86G5QT2DTQD20GGJ9EZYSS9", "01H86G5QT76Q8T1DXZK4APCFCE"]}'
WHERE
    id = 0x018a0d02df363138b01fdef1794c16a6;

INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02df4d345b03490265732dbb80,
    0x018a0d02de92199d3e9707431342954e,
    '2_9 回答未定問題 (回答: 未定)',
    '{}',
    3
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df4f90bce28415bbb90a2c56,
    0x018a0d02df4d345b03490265732dbb80,
    '2_9_1 回答1'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df53a0970e6caef4764d4bc9,
    0x018a0d02df4d345b03490265732dbb80,
    '2_9_2 回答2'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df58152d9cbd36fa5becfba9,
    0x018a0d02df4d345b03490265732dbb80,
    '2_9_3 回答3'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df5d96f377fc337d46aa3cf2,
    0x018a0d02df4d345b03490265732dbb80,
    '2_9_4 回答4'
);

INSERT INTO quizzes (
    id,
    event_id,
    body,
    correct_answer,
    type
)
VALUES (
    0x018a0d02df6107f2a4a9d606a22562a7,
    0x018a0d02de92199d3e9707431342954e,
    '2_10 並び替えクイズ (回答: 2 -> 4 -> 1 -> 3)',
    '{}',
    2
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df63c7407c230b58c27be855,
    0x018a0d02df6107f2a4a9d606a22562a7,
    '2_10_1 回答1'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df684731e6dd695ca611068b,
    0x018a0d02df6107f2a4a9d606a22562a7,
    '2_10_2 回答2'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df6e1fdc383c5dca79bc4ddd,
    0x018a0d02df6107f2a4a9d606a22562a7,
    '2_10_3 回答3'
);

INSERT INTO choices (
    id,
    quiz_id,
    body
)
VALUES (
    0x018a0d02df74591752330fa836d58151,
    0x018a0d02df6107f2a4a9d606a22562a7,
    '2_10_4 回答4'
);

UPDATE
    quizzes
SET
    correct_answer = '{"choiceIdList": ["01H86G5QV88WRYDQB9BJK121MB", "01H86G5QVMB4BN4CRFN0VDB0AH", "01H86G5QV3RX07R8RBB317QT2N", "01H86G5QVE3ZE3GF2XS9WVRKEX"]}'
WHERE
    id = 0x018a0d02df6107f2a4a9d606a22562a7;
