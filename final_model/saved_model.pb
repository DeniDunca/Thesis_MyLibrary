��
��
^
AssignVariableOp
resource
value"dtype"
dtypetype"
validate_shapebool( �
8
Const
output"dtype"
valuetensor"
dtypetype
$
DisableCopyOnRead
resource�
�
HashTableV2
table_handle"
	containerstring "
shared_namestring "!
use_node_name_sharingbool( "
	key_dtypetype"
value_dtypetype�
.
Identity

input"T
output"T"	
Ttype
w
LookupTableFindV2
table_handle
keys"Tin
default_value"Tout
values"Tout"
Tintype"
Touttype�
b
LookupTableImportV2
table_handle
keys"Tin
values"Tout"
Tintype"
Touttype�
u
MatMul
a"T
b"T
product"T"
transpose_abool( "
transpose_bbool( "
Ttype:
2	
�
MergeV2Checkpoints
checkpoint_prefixes
destination_prefix"
delete_old_dirsbool("
allow_missing_filesbool( �

NoOp
M
Pack
values"T*N
output"T"
Nint(0"	
Ttype"
axisint 
C
Placeholder
output"dtype"
dtypetype"
shapeshape:
@
ReadVariableOp
resource
value"dtype"
dtypetype�
�
ResourceGather
resource
indices"Tindices
output"dtype"

batch_dimsint "
validate_indicesbool("
dtypetype"
Tindicestype:
2	�
o
	RestoreV2

prefix
tensor_names
shape_and_slices
tensors2dtypes"
dtypes
list(type)(0�
l
SaveV2

prefix
tensor_names
shape_and_slices
tensors2dtypes"
dtypes
list(type)(0�
?
Select
	condition

t"T
e"T
output"T"	
Ttype
H
ShardedFilename
basename	
shard

num_shards
filename
�
StatefulPartitionedCall
args2Tin
output2Tout"
Tin
list(type)("
Tout
list(type)("	
ffunc"
configstring "
config_protostring "
executor_typestring ��
@
StaticRegexFullMatch	
input

output
"
patternstring
L

StringJoin
inputs*N

output"

Nint("
	separatorstring 
�
TopKV2

input"T
k"Tk
values"T
indices"
index_type"
sortedbool("
Ttype:
2	"
Tktype0:
2	"

index_typetype0:
2	
�
VarHandleOp
resource"
	containerstring "
shared_namestring "
dtypetype"
shapeshape"#
allowed_deviceslist(string)
 �"serve*2.14.0-dev202305292v1.12.1-94707-g727e706af398��
�
ConstConst*
_output_shapes
:i*
dtype0	*�
value�B�	i"�                                                        	       
                                                                                                                                                                  !       "       #       $       %       &       '       (       )       *       +       ,       -       .       /       0       1       2       3       4       5       6       7       8       9       :       ;       <       =       >       ?       @       A       B       C       D       E       F       G       H       I       J       K       L       M       N       O       P       Q       R       S       T       U       V       W       X       Y       Z       [       \       ]       ^       _       `       a       b       c       d       e       f       g       h       i       
�
Const_1Const*
_output_shapes
:i*
dtype0*�
value�B�iB0ICZIT3H8dcUuSwbdJvNa0xc4DG2B0mmA7F175fckFvkdRhtdjWsCgxn2B1QFUQmYLyZRIpIWN8uRhIU3yJPQ2B1oC0gb1SpdcObZudrTR0SGkbA9v1B1yYzlxvj75X5PYDimLVXoRngV0t1B2X05EVaSYhY1eZ8LihnyzC7ixXk1B2XKaJPqgCBhkewVavDos9tFD9RD2B2a7Hyks9JEUl2iKcd7Of1oSFUud2B3avc3TUJioP8XGD0bLK9xtV7uIG3B3rQeynAHLvdCCktkMEvmNH2lsBA3B5Ikb6gDlDtUa3mK2LHgZ4eJOZox1B5KTWlT2OWwRHug67r7jeTVfrhjD2B6GLbAghrIigUQdLi6C1JVBbotSM2B6I3XLOr5jdh6vwJOIukJtIe8qj43B6N1eLgXasCShAj54AyavIMrAawj2B6OAkrvyd46cl9VRIYzquM2pRt9a2B7J1ZKy8BeVRkLuM1sV6sNpuYiRy2B7qg0KhCnYnMpfvUcuv0gcs9O5bn2B87JsrZIxfLMCKrCsvVG0KdpOHQU2B8CwCgOdgwrVJAW1IWI2BPqSY5qG3B8OlhRZseDzcN3TXvEIDD3Gc1zos2BA6thDCd4yaZu2F9znvzMh3h5DZp1BAovDR0ooSqgOjKk5o4IgPdFvTt73BB0qBS7LJ1JYrULJ7SZCXjzr3eUC2BB4aEBppKyFWYEKeY8yDN8aT8Ha93BBbTNvgdEtTVeX6LkYtdLlrs77zI3BBfWy5TmlPjWMQSZuhMbub2vZdlw2BCPOHIRjuPRZoJMjrvkUtnFdGDeg2BCkABKdeMYESIrnhzxsAGN4AwLGC3BCkio6kBLdoZzxpSmrDL8Pra6FM53BDGhGftBPaEb16oYdXwoXO6DhvtK2BERbPBLjpo5YxGlvexBkYCIPpaw03BFahXkiZGFFYGSldWyWSIbQr88Pt2BGn6grkS47qa5ygm9YJqjRMJQJWE3BI77XVHjIgDYPDIepgeWDCyD2b4M2BIdsIJQeXtJPGcqB7nksuBVoOcpf1BIwMMVJ39DKOVaCVmJAJ1Tq3jI7n1BLa6W7HurPieX3MVe9tzifrLSeGa2BM9WdP1VPV3bxhRyqIlaTWf1yshz1BNPuNFMZ8vVf8FZRuAKeSBXsKVtz2BNsxvqp7GclhcWkLBHqh8vXzmc5x1BORsyiPtPPjfXeobTVZqT1qu2w7l2BOr9CWDRoNyfZfaocbYBveZpZOaf2BP4ZkdBaW1yaAeCtCEOSnLVY5YcM2BPgzb07La4DUNOhYPzYXHA7CdfNi1BQA82jYzDrxZz4VJIv3FuTQDCnDu2BQUB4Q0W64gXNDRYlY9pJRvOmd8S2BRORfT9iREjYZzuZJYVK5VCvdLQV2BRThynuCeqaOQSwQDMlLgCOwHM6H2BRj1H0OHNUZdvGGK4zCNWhTtq5AA3BT7ZDph39WGRAW63aZVvyCjTG4e52BTn1MI9rPRjN5mCdGHKZYcY8Vb9O2BUa0m8UY3BPXuX1cCgaB5PhdNKxd2BUuyOAvduETVRAtC3BudpLqyT2xQ2BV8gCJaMTTBW8aKqelXa8vZYEuTt1BVcleO2gLIXPTozaylbSxflXmUQ33BWA49GDsRvLZ0FiVdVipNpHTz79r1BXhgknpLHbuZGL9uidRkg6Cexvk42BYuO9mfnyGVPxMmgJ4TPZEmMSE192BYwIH7mZIE7ZFioFfFtSUxJfW1U33Ba5qfeUo5CUN3islt54flCaLaTux2BaCxjORrUTlOnK5eRPKsHdsSmcq23BapFcYvoRdOcP8gSzPc9qH9uJq6u1BdOTr3t5CL8XsoBVN5S9zvf0QcGp2BdZxY63YO8lOxLUlnAVCjYb8MdYd2BdwR2EfvoUQNgyMmLSxLNPWUcFzk1BeVVkYQnUouY3IVTph0t6XEpG6bC3BemTNkKzUsaP9a0YZLlwlNwoDEeJ3Bfk2jwP4q0AX1WrWBta8fFfgK2in2Bgr6OKGsoLXdaryhzUnu7JL4hVSb2Bh7iOUaur9wMUWhiKGlNIIJmRyxV2BhNFR9bitCTdqDHOYxwePr0la8Iv1BhUdrV32eosONegLsU3fBn4K6wF83Bha4a4HmixWQpgVWSMtjhpDKToDo1Bhh9IgnBg3BS0waCHzBqhI3mar4O2BiZrXFTqCMQeH0qlN6laWyW80Nmi1Bia3iUuz1j0VhQZyhcT0gGuaIAVz1Bj5Nei5amHvNLPsJmuRCMVHzDBeM2BjKD7Ix8eR2dCBdQ6dDGI5JW7Xns1BjlyytX1lv1QJWGPJ4f00K2LSxPH2Bk69vwymiAAPnl6KDqNQpFFDMlQg1BkFpYQATgVKPyTnhxhBCCPYY2GvO2BmMAqwdqvmoW1S5Rjb8z6nDLkQYm1BnDm8uUbyZWP6rRBSkQB6rUQTh2d2BndSp6u9gswWtzXyHEEDYqUI2zbE3BoYZG5cMs10XmkQNVKMZyCTJuHQ32BodQvJ6HMi8NqUOSrAvr716ZVuQ53Bp9VDClDO0mXaUxRJGEhuichjH3u2BprkgnGzuDIRKBlCTfsAVHMGDDwe2BqQ5Zj28vouNjrwDPNopnzMDlWY83BqsIrokwcklPZrSk2cVshwbunTw12BrEWNGJVTI1Q1KnS1TQUUDHnOCpy1BrKGMkUVOEZcqA9swmnbaSUtNHWz1BrrnzJl3aA0apiHJbM8DjATVVZZG3Brvbeq7LWWGUInb9wjX4I8RTWggC2BsAndYQe7CiWhHkXEFj1yt9NziNj1Bt7puKF0mswYgGJkSMYNnP4WbCSz1Bu27DpeeP2Jgnc1bJTPMgBRKR5jV2BuPTEbxiih7c5BXWDw3ncT1GeQQ73BusnzvJmCfSbTenjx1gOeSjWhZP03Bw73lJ3zGlrV74Hy9PJbAit9r7fE3BxL5ax8e2kmaNW0VFSgRopXi0O3o2ByRnxYQcQPjZ33F8oPVadEEHfT063BzFIBbPaNhrcyFTL3G5ZYmAOl9tR2BzwVJUfdC0oa9hWWp9uK0hRTM71j1
I
Const_2Const*
_output_shapes
: *
dtype0	*
value	B	 R 
�
StatefulPartitionedCallStatefulPartitionedCall*	
Tin
 *
Tout
2*
_collective_manager_ids
 *
_output_shapes
: * 
_read_only_resource_inputs
 *-
config_proto

CPU

GPU 2J 8� *2
f-R+
)__inference_restored_function_body_410958
�
embedding_17/embeddingsVarHandleOp*
_output_shapes
: *
dtype0*
shape
:j@*(
shared_nameembedding_17/embeddings
�
+embedding_17/embeddings/Read/ReadVariableOpReadVariableOpembedding_17/embeddings*
_output_shapes

:j@*
dtype0
q

candidatesVarHandleOp*
_output_shapes
: *
dtype0*
shape:	�@*
shared_name
candidates
j
candidates/Read/ReadVariableOpReadVariableOp
candidates*
_output_shapes
:	�@*
dtype0
o
identifiersVarHandleOp*
_output_shapes
: *
dtype0*
shape:�*
shared_nameidentifiers
h
identifiers/Read/ReadVariableOpReadVariableOpidentifiers*
_output_shapes	
:�*
dtype0
r
serving_default_input_1Placeholder*#
_output_shapes
:���������*
dtype0*
shape:���������
�
StatefulPartitionedCall_1StatefulPartitionedCallserving_default_input_1StatefulPartitionedCallConst_2embedding_17/embeddings
candidatesidentifiers*
Tin

2	*
Tout
2*
_collective_manager_ids
 *:
_output_shapes(
&:���������:���������*%
_read_only_resource_inputs
*-
config_proto

CPU

GPU 2J 8� *-
f(R&
$__inference_signature_wrapper_410897
�
StatefulPartitionedCall_2StatefulPartitionedCallStatefulPartitionedCallConst_1Const*
Tin
2	*
Tout
2*
_collective_manager_ids
 *&
 _has_manual_control_dependencies(*
_output_shapes
: * 
_read_only_resource_inputs
 *-
config_proto

CPU

GPU 2J 8� *(
f#R!
__inference__initializer_410941
(
NoOpNoOp^StatefulPartitionedCall_2
�
Const_3Const"/device:CPU:0*
_output_shapes
: *
dtype0*�
value�B� B�
�
	variables
trainable_variables
regularization_losses
	keras_api
__call__
*&call_and_return_all_conditional_losses
_default_save_signature
query_model
	identifiers
	_identifiers


candidates

_candidates
query_with_exclusions

signatures*

0
	1

2*

0*
* 
�
non_trainable_variables

layers
metrics
layer_regularization_losses
layer_metrics
	variables
trainable_variables
regularization_losses
__call__
_default_save_signature
*&call_and_return_all_conditional_losses
&"call_and_return_conditional_losses*

trace_0
trace_1* 

trace_0
trace_1* 

	capture_1* 
�
layer-0
layer_with_weights-0
layer-1
	variables
trainable_variables
regularization_losses
	keras_api
__call__
*&call_and_return_all_conditional_losses
# _self_saveable_object_factories*
KE
VARIABLE_VALUEidentifiers&identifiers/.ATTRIBUTES/VARIABLE_VALUE*
IC
VARIABLE_VALUE
candidates%candidates/.ATTRIBUTES/VARIABLE_VALUE*
* 

!serving_default* 
WQ
VARIABLE_VALUEembedding_17/embeddings&variables/0/.ATTRIBUTES/VARIABLE_VALUE*

	0

1*

0*
* 
* 
* 

	capture_1* 

	capture_1* 

	capture_1* 

	capture_1* 
* 
H
"	keras_api
#lookup_table
#$_self_saveable_object_factories* 
�
%	variables
&trainable_variables
'regularization_losses
(	keras_api
)__call__
**&call_and_return_all_conditional_losses

embeddings
#+_self_saveable_object_factories*

0*

0*
* 
�
,non_trainable_variables

-layers
.metrics
/layer_regularization_losses
0layer_metrics
	variables
trainable_variables
regularization_losses
__call__
*&call_and_return_all_conditional_losses
&"call_and_return_conditional_losses*

1trace_0
2trace_1* 

3trace_0
4trace_1* 
* 

	capture_1* 
* 
R
5_initializer
6_create_resource
7_initialize
8_destroy_resource* 
* 

0*

0*
* 
�
9non_trainable_variables

:layers
;metrics
<layer_regularization_losses
=layer_metrics
%	variables
&trainable_variables
'regularization_losses
)__call__
**&call_and_return_all_conditional_losses
&*"call_and_return_conditional_losses*

>trace_0* 

?trace_0* 
* 
* 

0
1*
* 
* 
* 

	capture_1* 

	capture_1* 

	capture_1* 

	capture_1* 
* 

@trace_0* 

Atrace_0* 

Btrace_0* 
* 
* 
* 
* 
* 
* 
* 
* 
 
C	capture_1
D	capture_2* 
* 
* 
* 
O
saver_filenamePlaceholder*
_output_shapes
: *
dtype0*
shape: 
�
StatefulPartitionedCall_3StatefulPartitionedCallsaver_filenameidentifiers
candidatesembedding_17/embeddingsConst_3*
Tin	
2*
Tout
2*
_collective_manager_ids
 *
_output_shapes
: * 
_read_only_resource_inputs
 *-
config_proto

CPU

GPU 2J 8� *(
f#R!
__inference__traced_save_411000
�
StatefulPartitionedCall_4StatefulPartitionedCallsaver_filenameidentifiers
candidatesembedding_17/embeddings*
Tin
2*
Tout
2*
_collective_manager_ids
 *
_output_shapes
: * 
_read_only_resource_inputs
 *-
config_proto

CPU

GPU 2J 8� *+
f&R$
"__inference__traced_restore_411018̕
�&
�
__inference__traced_save_411000
file_prefix1
"read_disablecopyonread_identifiers:	�6
#read_1_disablecopyonread_candidates:	�@B
0read_2_disablecopyonread_embedding_17_embeddings:j@
savev2_const_3

identity_7��MergeV2Checkpoints�Read/DisableCopyOnRead�Read/ReadVariableOp�Read_1/DisableCopyOnRead�Read_1/ReadVariableOp�Read_2/DisableCopyOnRead�Read_2/ReadVariableOpw
StaticRegexFullMatchStaticRegexFullMatchfile_prefix"/device:CPU:**
_output_shapes
: *
pattern
^s3://.*Z
ConstConst"/device:CPU:**
_output_shapes
: *
dtype0*
valueB B.parta
Const_1Const"/device:CPU:**
_output_shapes
: *
dtype0*
valueB B
_temp/part�
SelectSelectStaticRegexFullMatch:output:0Const:output:0Const_1:output:0"/device:CPU:**
T0*
_output_shapes
: f

StringJoin
StringJoinfile_prefixSelect:output:0"/device:CPU:**
N*
_output_shapes
: L

num_shardsConst*
_output_shapes
: *
dtype0*
value	B :f
ShardedFilename/shardConst"/device:CPU:0*
_output_shapes
: *
dtype0*
value	B : �
ShardedFilenameShardedFilenameStringJoin:output:0ShardedFilename/shard:output:0num_shards:output:0"/device:CPU:0*
_output_shapes
: t
Read/DisableCopyOnReadDisableCopyOnRead"read_disablecopyonread_identifiers"/device:CPU:0*
_output_shapes
 �
Read/ReadVariableOpReadVariableOp"read_disablecopyonread_identifiers^Read/DisableCopyOnRead"/device:CPU:0*
_output_shapes	
:�*
dtype0f
IdentityIdentityRead/ReadVariableOp:value:0"/device:CPU:0*
T0*
_output_shapes	
:�^

Identity_1IdentityIdentity:output:0"/device:CPU:0*
T0*
_output_shapes	
:�w
Read_1/DisableCopyOnReadDisableCopyOnRead#read_1_disablecopyonread_candidates"/device:CPU:0*
_output_shapes
 �
Read_1/ReadVariableOpReadVariableOp#read_1_disablecopyonread_candidates^Read_1/DisableCopyOnRead"/device:CPU:0*
_output_shapes
:	�@*
dtype0n

Identity_2IdentityRead_1/ReadVariableOp:value:0"/device:CPU:0*
T0*
_output_shapes
:	�@d

Identity_3IdentityIdentity_2:output:0"/device:CPU:0*
T0*
_output_shapes
:	�@�
Read_2/DisableCopyOnReadDisableCopyOnRead0read_2_disablecopyonread_embedding_17_embeddings"/device:CPU:0*
_output_shapes
 �
Read_2/ReadVariableOpReadVariableOp0read_2_disablecopyonread_embedding_17_embeddings^Read_2/DisableCopyOnRead"/device:CPU:0*
_output_shapes

:j@*
dtype0m

Identity_4IdentityRead_2/ReadVariableOp:value:0"/device:CPU:0*
T0*
_output_shapes

:j@c

Identity_5IdentityIdentity_4:output:0"/device:CPU:0*
T0*
_output_shapes

:j@�
SaveV2/tensor_namesConst"/device:CPU:0*
_output_shapes
:*
dtype0*�
value�B�B&identifiers/.ATTRIBUTES/VARIABLE_VALUEB%candidates/.ATTRIBUTES/VARIABLE_VALUEB&variables/0/.ATTRIBUTES/VARIABLE_VALUEB_CHECKPOINTABLE_OBJECT_GRAPHu
SaveV2/shape_and_slicesConst"/device:CPU:0*
_output_shapes
:*
dtype0*
valueBB B B B �
SaveV2SaveV2ShardedFilename:filename:0SaveV2/tensor_names:output:0 SaveV2/shape_and_slices:output:0Identity_1:output:0Identity_3:output:0Identity_5:output:0savev2_const_3"/device:CPU:0*&
 _has_manual_control_dependencies(*
_output_shapes
 *
dtypes
2�
&MergeV2Checkpoints/checkpoint_prefixesPackShardedFilename:filename:0^SaveV2"/device:CPU:0*
N*
T0*
_output_shapes
:�
MergeV2CheckpointsMergeV2Checkpoints/MergeV2Checkpoints/checkpoint_prefixes:output:0file_prefix"/device:CPU:0*&
 _has_manual_control_dependencies(*
_output_shapes
 h

Identity_6Identityfile_prefix^MergeV2Checkpoints"/device:CPU:0*
T0*
_output_shapes
: S

Identity_7IdentityIdentity_6:output:0^NoOp*
T0*
_output_shapes
: �
NoOpNoOp^MergeV2Checkpoints^Read/DisableCopyOnRead^Read/ReadVariableOp^Read_1/DisableCopyOnRead^Read_1/ReadVariableOp^Read_2/DisableCopyOnRead^Read_2/ReadVariableOp*
_output_shapes
 "!

identity_7Identity_7:output:0*(
_construction_contextkEagerRuntime*
_input_shapes

: : : : : 2(
MergeV2CheckpointsMergeV2Checkpoints20
Read/DisableCopyOnReadRead/DisableCopyOnRead2*
Read/ReadVariableOpRead/ReadVariableOp24
Read_1/DisableCopyOnReadRead_1/DisableCopyOnRead2.
Read_1/ReadVariableOpRead_1/ReadVariableOp24
Read_2/DisableCopyOnReadRead_2/DisableCopyOnRead2.
Read_2/ReadVariableOpRead_2/ReadVariableOp:?;

_output_shapes
: 
!
_user_specified_name	Const_3:73
1
_user_specified_nameembedding_17/embeddings:*&
$
_user_specified_name
candidates:+'
%
_user_specified_nameidentifiers:C ?

_output_shapes
: 
%
_user_specified_namefile_prefix
�
-
__inference__destroyer_408295
identityG
ConstConst*
_output_shapes
: *
dtype0*
value	B :E
IdentityIdentityConst:output:0*
T0*
_output_shapes
: "
identityIdentity:output:0*(
_construction_contextkEagerRuntime*
_input_shapes 
�
�
H__inference_embedding_17_layer_call_and_return_conditional_losses_410912

inputs	)
embedding_lookup_410907:j@
identity��embedding_lookup�
embedding_lookupResourceGatherembedding_lookup_410907inputs*
Tindices0	**
_class 
loc:@embedding_lookup/410907*'
_output_shapes
:���������@*
dtype0r
embedding_lookup/IdentityIdentityembedding_lookup:output:0*
T0*'
_output_shapes
:���������@q
IdentityIdentity"embedding_lookup/Identity:output:0^NoOp*
T0*'
_output_shapes
:���������@5
NoOpNoOp^embedding_lookup*
_output_shapes
 "
identityIdentity:output:0*(
_construction_contextkEagerRuntime*$
_input_shapes
:���������: 2$
embedding_lookupembedding_lookup:&"
 
_user_specified_name410907:K G
#
_output_shapes
:���������
 
_user_specified_nameinputs
�
�
.__inference_sequential_25_layer_call_fn_410787
string_lookup_17_input
unknown
	unknown_0	
	unknown_1:j@
identity��StatefulPartitionedCall�
StatefulPartitionedCallStatefulPartitionedCallstring_lookup_17_inputunknown	unknown_0	unknown_1*
Tin
2	*
Tout
2*
_collective_manager_ids
 *'
_output_shapes
:���������@*#
_read_only_resource_inputs
*-
config_proto

CPU

GPU 2J 8� *R
fMRK
I__inference_sequential_25_layer_call_and_return_conditional_losses_410765o
IdentityIdentity StatefulPartitionedCall:output:0^NoOp*
T0*'
_output_shapes
:���������@<
NoOpNoOp^StatefulPartitionedCall*
_output_shapes
 "
identityIdentity:output:0*(
_construction_contextkEagerRuntime*(
_input_shapes
:���������: : : 22
StatefulPartitionedCallStatefulPartitionedCall:&"
 
_user_specified_name410783:

_output_shapes
: :&"
 
_user_specified_name410779:[ W
#
_output_shapes
:���������
0
_user_specified_namestring_lookup_17_input
�
�
.__inference_brute_force_2_layer_call_fn_410879
input_1
unknown
	unknown_0	
	unknown_1:j@
	unknown_2:	�@
	unknown_3:	�
identity

identity_1��StatefulPartitionedCall�
StatefulPartitionedCallStatefulPartitionedCallinput_1unknown	unknown_0	unknown_1	unknown_2	unknown_3*
Tin

2	*
Tout
2*
_collective_manager_ids
 *:
_output_shapes(
&:���������:���������*%
_read_only_resource_inputs
*-
config_proto

CPU

GPU 2J 8� *R
fMRK
I__inference_brute_force_2_layer_call_and_return_conditional_losses_410845o
IdentityIdentity StatefulPartitionedCall:output:0^NoOp*
T0*'
_output_shapes
:���������q

Identity_1Identity StatefulPartitionedCall:output:1^NoOp*
T0*'
_output_shapes
:���������<
NoOpNoOp^StatefulPartitionedCall*
_output_shapes
 "!

identity_1Identity_1:output:0"
identityIdentity:output:0*(
_construction_contextkEagerRuntime*,
_input_shapes
:���������: : : : : 22
StatefulPartitionedCallStatefulPartitionedCall:&"
 
_user_specified_name410873:&"
 
_user_specified_name410871:&"
 
_user_specified_name410869:

_output_shapes
: :&"
 
_user_specified_name410865:L H
#
_output_shapes
:���������
!
_user_specified_name	input_1
�
�
I__inference_sequential_25_layer_call_and_return_conditional_losses_410776
string_lookup_17_input?
;string_lookup_17_none_lookup_lookuptablefindv2_table_handle@
<string_lookup_17_none_lookup_lookuptablefindv2_default_value	%
embedding_17_410772:j@
identity��$embedding_17/StatefulPartitionedCall�.string_lookup_17/None_Lookup/LookupTableFindV2�
.string_lookup_17/None_Lookup/LookupTableFindV2LookupTableFindV2;string_lookup_17_none_lookup_lookuptablefindv2_table_handlestring_lookup_17_input<string_lookup_17_none_lookup_lookuptablefindv2_default_value*	
Tin0*

Tout0	*#
_output_shapes
:����������
string_lookup_17/IdentityIdentity7string_lookup_17/None_Lookup/LookupTableFindV2:values:0*
T0	*#
_output_shapes
:����������
$embedding_17/StatefulPartitionedCallStatefulPartitionedCall"string_lookup_17/Identity:output:0embedding_17_410772*
Tin
2	*
Tout
2*
_collective_manager_ids
 *'
_output_shapes
:���������@*#
_read_only_resource_inputs
*-
config_proto

CPU

GPU 2J 8� *Q
fLRJ
H__inference_embedding_17_layer_call_and_return_conditional_losses_410760|
IdentityIdentity-embedding_17/StatefulPartitionedCall:output:0^NoOp*
T0*'
_output_shapes
:���������@z
NoOpNoOp%^embedding_17/StatefulPartitionedCall/^string_lookup_17/None_Lookup/LookupTableFindV2*
_output_shapes
 "
identityIdentity:output:0*(
_construction_contextkEagerRuntime*(
_input_shapes
:���������: : : 2L
$embedding_17/StatefulPartitionedCall$embedding_17/StatefulPartitionedCall2`
.string_lookup_17/None_Lookup/LookupTableFindV2.string_lookup_17/None_Lookup/LookupTableFindV2:&"
 
_user_specified_name410772:

_output_shapes
: :,(
&
_user_specified_nametable_handle:[ W
#
_output_shapes
:���������
0
_user_specified_namestring_lookup_17_input
�
�
"__inference__traced_restore_411018
file_prefix+
assignvariableop_identifiers:	�0
assignvariableop_1_candidates:	�@<
*assignvariableop_2_embedding_17_embeddings:j@

identity_4��AssignVariableOp�AssignVariableOp_1�AssignVariableOp_2�
RestoreV2/tensor_namesConst"/device:CPU:0*
_output_shapes
:*
dtype0*�
value�B�B&identifiers/.ATTRIBUTES/VARIABLE_VALUEB%candidates/.ATTRIBUTES/VARIABLE_VALUEB&variables/0/.ATTRIBUTES/VARIABLE_VALUEB_CHECKPOINTABLE_OBJECT_GRAPHx
RestoreV2/shape_and_slicesConst"/device:CPU:0*
_output_shapes
:*
dtype0*
valueBB B B B �
	RestoreV2	RestoreV2file_prefixRestoreV2/tensor_names:output:0#RestoreV2/shape_and_slices:output:0"/device:CPU:0*$
_output_shapes
::::*
dtypes
2[
IdentityIdentityRestoreV2:tensors:0"/device:CPU:0*
T0*
_output_shapes
:�
AssignVariableOpAssignVariableOpassignvariableop_identifiersIdentity:output:0"/device:CPU:0*&
 _has_manual_control_dependencies(*
_output_shapes
 *
dtype0]

Identity_1IdentityRestoreV2:tensors:1"/device:CPU:0*
T0*
_output_shapes
:�
AssignVariableOp_1AssignVariableOpassignvariableop_1_candidatesIdentity_1:output:0"/device:CPU:0*&
 _has_manual_control_dependencies(*
_output_shapes
 *
dtype0]

Identity_2IdentityRestoreV2:tensors:2"/device:CPU:0*
T0*
_output_shapes
:�
AssignVariableOp_2AssignVariableOp*assignvariableop_2_embedding_17_embeddingsIdentity_2:output:0"/device:CPU:0*&
 _has_manual_control_dependencies(*
_output_shapes
 *
dtype0Y
NoOpNoOp"/device:CPU:0*&
 _has_manual_control_dependencies(*
_output_shapes
 �

Identity_3Identityfile_prefix^AssignVariableOp^AssignVariableOp_1^AssignVariableOp_2^NoOp"/device:CPU:0*
T0*
_output_shapes
: U

Identity_4IdentityIdentity_3:output:0^NoOp_1*
T0*
_output_shapes
: a
NoOp_1NoOp^AssignVariableOp^AssignVariableOp_1^AssignVariableOp_2*
_output_shapes
 "!

identity_4Identity_4:output:0*(
_construction_contextkEagerRuntime*
_input_shapes

: : : : 2(
AssignVariableOp_1AssignVariableOp_12(
AssignVariableOp_2AssignVariableOp_22$
AssignVariableOpAssignVariableOp:73
1
_user_specified_nameembedding_17/embeddings:*&
$
_user_specified_name
candidates:+'
%
_user_specified_nameidentifiers:C ?

_output_shapes
: 
%
_user_specified_namefile_prefix
�
�
)__inference_restored_function_body_410931
unknown
	unknown_0
	unknown_1	
identity��StatefulPartitionedCall�
StatefulPartitionedCallStatefulPartitionedCallunknown	unknown_0	unknown_1*
Tin
2	*
Tout
2*
_collective_manager_ids
 *
_output_shapes
: * 
_read_only_resource_inputs
 *-
config_proto

CPU

GPU 2J 8� *(
f#R!
__inference__initializer_408423^
IdentityIdentity StatefulPartitionedCall:output:0^NoOp*
T0*
_output_shapes
: <
NoOpNoOp^StatefulPartitionedCall*
_output_shapes
 "
identityIdentity:output:0*(
_construction_contextkEagerRuntime*!
_input_shapes
: :i:i22
StatefulPartitionedCallStatefulPartitionedCall: 

_output_shapes
:i: 

_output_shapes
:i:& "
 
_user_specified_name410923
�

�
$__inference_signature_wrapper_410897
input_1
unknown
	unknown_0	
	unknown_1:j@
	unknown_2:	�@
	unknown_3:	�
identity

identity_1��StatefulPartitionedCall�
StatefulPartitionedCallStatefulPartitionedCallinput_1unknown	unknown_0	unknown_1	unknown_2	unknown_3*
Tin

2	*
Tout
2*
_collective_manager_ids
 *:
_output_shapes(
&:���������:���������*%
_read_only_resource_inputs
*-
config_proto

CPU

GPU 2J 8� **
f%R#
!__inference__wrapped_model_410746o
IdentityIdentity StatefulPartitionedCall:output:0^NoOp*
T0*'
_output_shapes
:���������q

Identity_1Identity StatefulPartitionedCall:output:1^NoOp*
T0*'
_output_shapes
:���������<
NoOpNoOp^StatefulPartitionedCall*
_output_shapes
 "!

identity_1Identity_1:output:0"
identityIdentity:output:0*(
_construction_contextkEagerRuntime*,
_input_shapes
:���������: : : : : 22
StatefulPartitionedCallStatefulPartitionedCall:&"
 
_user_specified_name410891:&"
 
_user_specified_name410889:&"
 
_user_specified_name410887:

_output_shapes
: :&"
 
_user_specified_name410883:L H
#
_output_shapes
:���������
!
_user_specified_name	input_1
�
H
__inference__creator_410920
identity��StatefulPartitionedCall�
StatefulPartitionedCallStatefulPartitionedCall*	
Tin
 *
Tout
2*
_collective_manager_ids
 *
_output_shapes
: * 
_read_only_resource_inputs
 *-
config_proto

CPU

GPU 2J 8� *2
f-R+
)__inference_restored_function_body_410917^
IdentityIdentity StatefulPartitionedCall:output:0^NoOp*
T0*
_output_shapes
: <
NoOpNoOp^StatefulPartitionedCall*
_output_shapes
 "
identityIdentity:output:0*(
_construction_contextkEagerRuntime*
_input_shapes 22
StatefulPartitionedCallStatefulPartitionedCall
�
V
)__inference_restored_function_body_410958
identity��StatefulPartitionedCall�
StatefulPartitionedCallStatefulPartitionedCall*	
Tin
 *
Tout
2*
_collective_manager_ids
 *
_output_shapes
: * 
_read_only_resource_inputs
 *-
config_proto

CPU

GPU 2J 8� *$
fR
__inference__creator_409064^
IdentityIdentity StatefulPartitionedCall:output:0^NoOp*
T0*
_output_shapes
: <
NoOpNoOp^StatefulPartitionedCall*
_output_shapes
 "
identityIdentity:output:0*(
_construction_contextkEagerRuntime*
_input_shapes 22
StatefulPartitionedCallStatefulPartitionedCall
�
9
)__inference_restored_function_body_410946
identity�
PartitionedCallPartitionedCall*	
Tin
 *
Tout
2*
_collective_manager_ids
 *
_output_shapes
: * 
_read_only_resource_inputs
 *-
config_proto

CPU

GPU 2J 8� *&
f!R
__inference__destroyer_408295O
IdentityIdentityPartitionedCall:output:0*
T0*
_output_shapes
: "
identityIdentity:output:0*(
_construction_contextkEagerRuntime*
_input_shapes 
�
�
.__inference_sequential_25_layer_call_fn_410798
string_lookup_17_input
unknown
	unknown_0	
	unknown_1:j@
identity��StatefulPartitionedCall�
StatefulPartitionedCallStatefulPartitionedCallstring_lookup_17_inputunknown	unknown_0	unknown_1*
Tin
2	*
Tout
2*
_collective_manager_ids
 *'
_output_shapes
:���������@*#
_read_only_resource_inputs
*-
config_proto

CPU

GPU 2J 8� *R
fMRK
I__inference_sequential_25_layer_call_and_return_conditional_losses_410776o
IdentityIdentity StatefulPartitionedCall:output:0^NoOp*
T0*'
_output_shapes
:���������@<
NoOpNoOp^StatefulPartitionedCall*
_output_shapes
 "
identityIdentity:output:0*(
_construction_contextkEagerRuntime*(
_input_shapes
:���������: : : 22
StatefulPartitionedCallStatefulPartitionedCall:&"
 
_user_specified_name410794:

_output_shapes
: :&"
 
_user_specified_name410790:[ W
#
_output_shapes
:���������
0
_user_specified_namestring_lookup_17_input
�
;
__inference__creator_409064
identity��
hash_table�

hash_tableHashTableV2*
_output_shapes
: *
	key_dtype0**
shared_name344731_load_408252_409060*
use_node_name_sharing(*
value_dtype0	/
NoOpNoOp^hash_table*
_output_shapes
 W
IdentityIdentityhash_table:table_handle:0^NoOp*
T0*
_output_shapes
: "
identityIdentity:output:0*(
_construction_contextkEagerRuntime*
_input_shapes 2

hash_table
hash_table
�
�
__inference__initializer_4084239
5key_value_init344730_lookuptableimportv2_table_handle1
-key_value_init344730_lookuptableimportv2_keys3
/key_value_init344730_lookuptableimportv2_values	
identity��(key_value_init344730/LookupTableImportV2�
(key_value_init344730/LookupTableImportV2LookupTableImportV25key_value_init344730_lookuptableimportv2_table_handle-key_value_init344730_lookuptableimportv2_keys/key_value_init344730_lookuptableimportv2_values*	
Tin0*

Tout0	*
_output_shapes
 G
ConstConst*
_output_shapes
: *
dtype0*
value	B :M
NoOpNoOp)^key_value_init344730/LookupTableImportV2*
_output_shapes
 L
IdentityIdentityConst:output:0^NoOp*
T0*
_output_shapes
: "
identityIdentity:output:0*(
_construction_contextkEagerRuntime*!
_input_shapes
: :i:i2T
(key_value_init344730/LookupTableImportV2(key_value_init344730/LookupTableImportV2: 

_output_shapes
:i: 

_output_shapes
:i:, (
&
_user_specified_nametable_handle
�
�
-__inference_embedding_17_layer_call_fn_410904

inputs	
unknown:j@
identity��StatefulPartitionedCall�
StatefulPartitionedCallStatefulPartitionedCallinputsunknown*
Tin
2	*
Tout
2*
_collective_manager_ids
 *'
_output_shapes
:���������@*#
_read_only_resource_inputs
*-
config_proto

CPU

GPU 2J 8� *Q
fLRJ
H__inference_embedding_17_layer_call_and_return_conditional_losses_410760o
IdentityIdentity StatefulPartitionedCall:output:0^NoOp*
T0*'
_output_shapes
:���������@<
NoOpNoOp^StatefulPartitionedCall*
_output_shapes
 "
identityIdentity:output:0*(
_construction_contextkEagerRuntime*$
_input_shapes
:���������: 22
StatefulPartitionedCallStatefulPartitionedCall:&"
 
_user_specified_name410900:K G
#
_output_shapes
:���������
 
_user_specified_nameinputs
�
�
!__inference__wrapped_model_410746
input_1[
Wbrute_force_2_sequential_25_string_lookup_17_none_lookup_lookuptablefindv2_table_handle\
Xbrute_force_2_sequential_25_string_lookup_17_none_lookup_lookuptablefindv2_default_value	R
@brute_force_2_sequential_25_embedding_17_embedding_lookup_410732:j@?
,brute_force_2_matmul_readvariableop_resource:	�@,
brute_force_2_gather_resource:	�
identity

identity_1��brute_force_2/Gather�#brute_force_2/MatMul/ReadVariableOp�9brute_force_2/sequential_25/embedding_17/embedding_lookup�Jbrute_force_2/sequential_25/string_lookup_17/None_Lookup/LookupTableFindV2�
Jbrute_force_2/sequential_25/string_lookup_17/None_Lookup/LookupTableFindV2LookupTableFindV2Wbrute_force_2_sequential_25_string_lookup_17_none_lookup_lookuptablefindv2_table_handleinput_1Xbrute_force_2_sequential_25_string_lookup_17_none_lookup_lookuptablefindv2_default_value*	
Tin0*

Tout0	*#
_output_shapes
:����������
5brute_force_2/sequential_25/string_lookup_17/IdentityIdentitySbrute_force_2/sequential_25/string_lookup_17/None_Lookup/LookupTableFindV2:values:0*
T0	*#
_output_shapes
:����������
9brute_force_2/sequential_25/embedding_17/embedding_lookupResourceGather@brute_force_2_sequential_25_embedding_17_embedding_lookup_410732>brute_force_2/sequential_25/string_lookup_17/Identity:output:0*
Tindices0	*S
_classI
GEloc:@brute_force_2/sequential_25/embedding_17/embedding_lookup/410732*'
_output_shapes
:���������@*
dtype0�
Bbrute_force_2/sequential_25/embedding_17/embedding_lookup/IdentityIdentityBbrute_force_2/sequential_25/embedding_17/embedding_lookup:output:0*
T0*'
_output_shapes
:���������@�
#brute_force_2/MatMul/ReadVariableOpReadVariableOp,brute_force_2_matmul_readvariableop_resource*
_output_shapes
:	�@*
dtype0�
brute_force_2/MatMulMatMulKbrute_force_2/sequential_25/embedding_17/embedding_lookup/Identity:output:0+brute_force_2/MatMul/ReadVariableOp:value:0*
T0*(
_output_shapes
:����������*
transpose_b(X
brute_force_2/TopKV2/kConst*
_output_shapes
: *
dtype0*
value	B :�
brute_force_2/TopKV2TopKV2brute_force_2/MatMul:product:0brute_force_2/TopKV2/k:output:0*
T0*:
_output_shapes(
&:���������:����������
brute_force_2/GatherResourceGatherbrute_force_2_gather_resourcebrute_force_2/TopKV2:indices:0*
Tindices0*'
_output_shapes
:���������*
dtype0l
IdentityIdentitybrute_force_2/TopKV2:values:0^NoOp*
T0*'
_output_shapes
:���������n

Identity_1Identitybrute_force_2/Gather:output:0^NoOp*
T0*'
_output_shapes
:����������
NoOpNoOp^brute_force_2/Gather$^brute_force_2/MatMul/ReadVariableOp:^brute_force_2/sequential_25/embedding_17/embedding_lookupK^brute_force_2/sequential_25/string_lookup_17/None_Lookup/LookupTableFindV2*
_output_shapes
 "!

identity_1Identity_1:output:0"
identityIdentity:output:0*(
_construction_contextkEagerRuntime*,
_input_shapes
:���������: : : : : 2,
brute_force_2/Gatherbrute_force_2/Gather2J
#brute_force_2/MatMul/ReadVariableOp#brute_force_2/MatMul/ReadVariableOp2v
9brute_force_2/sequential_25/embedding_17/embedding_lookup9brute_force_2/sequential_25/embedding_17/embedding_lookup2�
Jbrute_force_2/sequential_25/string_lookup_17/None_Lookup/LookupTableFindV2Jbrute_force_2/sequential_25/string_lookup_17/None_Lookup/LookupTableFindV2:($
"
_user_specified_name
resource:($
"
_user_specified_name
resource:&"
 
_user_specified_name410732:

_output_shapes
: :,(
&
_user_specified_nametable_handle:L H
#
_output_shapes
:���������
!
_user_specified_name	input_1
�
�
I__inference_brute_force_2_layer_call_and_return_conditional_losses_410845
input_1
sequential_25_410828
sequential_25_410830	&
sequential_25_410832:j@1
matmul_readvariableop_resource:	�@
gather_resource:	�
identity

identity_1��Gather�MatMul/ReadVariableOp�%sequential_25/StatefulPartitionedCall�
%sequential_25/StatefulPartitionedCallStatefulPartitionedCallinput_1sequential_25_410828sequential_25_410830sequential_25_410832*
Tin
2	*
Tout
2*
_collective_manager_ids
 *'
_output_shapes
:���������@*#
_read_only_resource_inputs
*-
config_proto

CPU

GPU 2J 8� *R
fMRK
I__inference_sequential_25_layer_call_and_return_conditional_losses_410776u
MatMul/ReadVariableOpReadVariableOpmatmul_readvariableop_resource*
_output_shapes
:	�@*
dtype0�
MatMulMatMul.sequential_25/StatefulPartitionedCall:output:0MatMul/ReadVariableOp:value:0*
T0*(
_output_shapes
:����������*
transpose_b(J
TopKV2/kConst*
_output_shapes
: *
dtype0*
value	B :z
TopKV2TopKV2MatMul:product:0TopKV2/k:output:0*
T0*:
_output_shapes(
&:���������:����������
GatherResourceGathergather_resourceTopKV2:indices:0*
Tindices0*'
_output_shapes
:���������*
dtype0^
IdentityIdentityTopKV2:values:0^NoOp*
T0*'
_output_shapes
:���������`

Identity_1IdentityGather:output:0^NoOp*
T0*'
_output_shapes
:���������k
NoOpNoOp^Gather^MatMul/ReadVariableOp&^sequential_25/StatefulPartitionedCall*
_output_shapes
 "!

identity_1Identity_1:output:0"
identityIdentity:output:0*(
_construction_contextkEagerRuntime*,
_input_shapes
:���������: : : : : 2
GatherGather2.
MatMul/ReadVariableOpMatMul/ReadVariableOp2N
%sequential_25/StatefulPartitionedCall%sequential_25/StatefulPartitionedCall:($
"
_user_specified_name
resource:($
"
_user_specified_name
resource:&"
 
_user_specified_name410832:

_output_shapes
: :&"
 
_user_specified_name410828:L H
#
_output_shapes
:���������
!
_user_specified_name	input_1
�
�
I__inference_brute_force_2_layer_call_and_return_conditional_losses_410825
input_1
sequential_25_410808
sequential_25_410810	&
sequential_25_410812:j@1
matmul_readvariableop_resource:	�@
gather_resource:	�
identity

identity_1��Gather�MatMul/ReadVariableOp�%sequential_25/StatefulPartitionedCall�
%sequential_25/StatefulPartitionedCallStatefulPartitionedCallinput_1sequential_25_410808sequential_25_410810sequential_25_410812*
Tin
2	*
Tout
2*
_collective_manager_ids
 *'
_output_shapes
:���������@*#
_read_only_resource_inputs
*-
config_proto

CPU

GPU 2J 8� *R
fMRK
I__inference_sequential_25_layer_call_and_return_conditional_losses_410765u
MatMul/ReadVariableOpReadVariableOpmatmul_readvariableop_resource*
_output_shapes
:	�@*
dtype0�
MatMulMatMul.sequential_25/StatefulPartitionedCall:output:0MatMul/ReadVariableOp:value:0*
T0*(
_output_shapes
:����������*
transpose_b(J
TopKV2/kConst*
_output_shapes
: *
dtype0*
value	B :z
TopKV2TopKV2MatMul:product:0TopKV2/k:output:0*
T0*:
_output_shapes(
&:���������:����������
GatherResourceGathergather_resourceTopKV2:indices:0*
Tindices0*'
_output_shapes
:���������*
dtype0^
IdentityIdentityTopKV2:values:0^NoOp*
T0*'
_output_shapes
:���������`

Identity_1IdentityGather:output:0^NoOp*
T0*'
_output_shapes
:���������k
NoOpNoOp^Gather^MatMul/ReadVariableOp&^sequential_25/StatefulPartitionedCall*
_output_shapes
 "!

identity_1Identity_1:output:0"
identityIdentity:output:0*(
_construction_contextkEagerRuntime*,
_input_shapes
:���������: : : : : 2
GatherGather2.
MatMul/ReadVariableOpMatMul/ReadVariableOp2N
%sequential_25/StatefulPartitionedCall%sequential_25/StatefulPartitionedCall:($
"
_user_specified_name
resource:($
"
_user_specified_name
resource:&"
 
_user_specified_name410812:

_output_shapes
: :&"
 
_user_specified_name410808:L H
#
_output_shapes
:���������
!
_user_specified_name	input_1
�
V
)__inference_restored_function_body_410917
identity��StatefulPartitionedCall�
StatefulPartitionedCallStatefulPartitionedCall*	
Tin
 *
Tout
2*
_collective_manager_ids
 *
_output_shapes
: * 
_read_only_resource_inputs
 *-
config_proto

CPU

GPU 2J 8� *$
fR
__inference__creator_409064^
IdentityIdentity StatefulPartitionedCall:output:0^NoOp*
T0*
_output_shapes
: <
NoOpNoOp^StatefulPartitionedCall*
_output_shapes
 "
identityIdentity:output:0*(
_construction_contextkEagerRuntime*
_input_shapes 22
StatefulPartitionedCallStatefulPartitionedCall
�
�
H__inference_embedding_17_layer_call_and_return_conditional_losses_410760

inputs	)
embedding_lookup_410755:j@
identity��embedding_lookup�
embedding_lookupResourceGatherembedding_lookup_410755inputs*
Tindices0	**
_class 
loc:@embedding_lookup/410755*'
_output_shapes
:���������@*
dtype0r
embedding_lookup/IdentityIdentityembedding_lookup:output:0*
T0*'
_output_shapes
:���������@q
IdentityIdentity"embedding_lookup/Identity:output:0^NoOp*
T0*'
_output_shapes
:���������@5
NoOpNoOp^embedding_lookup*
_output_shapes
 "
identityIdentity:output:0*(
_construction_contextkEagerRuntime*$
_input_shapes
:���������: 2$
embedding_lookupembedding_lookup:&"
 
_user_specified_name410755:K G
#
_output_shapes
:���������
 
_user_specified_nameinputs
�
�
.__inference_brute_force_2_layer_call_fn_410862
input_1
unknown
	unknown_0	
	unknown_1:j@
	unknown_2:	�@
	unknown_3:	�
identity

identity_1��StatefulPartitionedCall�
StatefulPartitionedCallStatefulPartitionedCallinput_1unknown	unknown_0	unknown_1	unknown_2	unknown_3*
Tin

2	*
Tout
2*
_collective_manager_ids
 *:
_output_shapes(
&:���������:���������*%
_read_only_resource_inputs
*-
config_proto

CPU

GPU 2J 8� *R
fMRK
I__inference_brute_force_2_layer_call_and_return_conditional_losses_410825o
IdentityIdentity StatefulPartitionedCall:output:0^NoOp*
T0*'
_output_shapes
:���������q

Identity_1Identity StatefulPartitionedCall:output:1^NoOp*
T0*'
_output_shapes
:���������<
NoOpNoOp^StatefulPartitionedCall*
_output_shapes
 "!

identity_1Identity_1:output:0"
identityIdentity:output:0*(
_construction_contextkEagerRuntime*,
_input_shapes
:���������: : : : : 22
StatefulPartitionedCallStatefulPartitionedCall:&"
 
_user_specified_name410856:&"
 
_user_specified_name410854:&"
 
_user_specified_name410852:

_output_shapes
: :&"
 
_user_specified_name410848:L H
#
_output_shapes
:���������
!
_user_specified_name	input_1
�
-
__inference__destroyer_410950
identity�
PartitionedCallPartitionedCall*	
Tin
 *
Tout
2*
_collective_manager_ids
 *
_output_shapes
: * 
_read_only_resource_inputs
 *-
config_proto

CPU

GPU 2J 8� *2
f-R+
)__inference_restored_function_body_410946G
ConstConst*
_output_shapes
: *
dtype0*
value	B :E
IdentityIdentityConst:output:0*
T0*
_output_shapes
: "
identityIdentity:output:0*(
_construction_contextkEagerRuntime*
_input_shapes 
�
�
I__inference_sequential_25_layer_call_and_return_conditional_losses_410765
string_lookup_17_input?
;string_lookup_17_none_lookup_lookuptablefindv2_table_handle@
<string_lookup_17_none_lookup_lookuptablefindv2_default_value	%
embedding_17_410761:j@
identity��$embedding_17/StatefulPartitionedCall�.string_lookup_17/None_Lookup/LookupTableFindV2�
.string_lookup_17/None_Lookup/LookupTableFindV2LookupTableFindV2;string_lookup_17_none_lookup_lookuptablefindv2_table_handlestring_lookup_17_input<string_lookup_17_none_lookup_lookuptablefindv2_default_value*	
Tin0*

Tout0	*#
_output_shapes
:����������
string_lookup_17/IdentityIdentity7string_lookup_17/None_Lookup/LookupTableFindV2:values:0*
T0	*#
_output_shapes
:����������
$embedding_17/StatefulPartitionedCallStatefulPartitionedCall"string_lookup_17/Identity:output:0embedding_17_410761*
Tin
2	*
Tout
2*
_collective_manager_ids
 *'
_output_shapes
:���������@*#
_read_only_resource_inputs
*-
config_proto

CPU

GPU 2J 8� *Q
fLRJ
H__inference_embedding_17_layer_call_and_return_conditional_losses_410760|
IdentityIdentity-embedding_17/StatefulPartitionedCall:output:0^NoOp*
T0*'
_output_shapes
:���������@z
NoOpNoOp%^embedding_17/StatefulPartitionedCall/^string_lookup_17/None_Lookup/LookupTableFindV2*
_output_shapes
 "
identityIdentity:output:0*(
_construction_contextkEagerRuntime*(
_input_shapes
:���������: : : 2L
$embedding_17/StatefulPartitionedCall$embedding_17/StatefulPartitionedCall2`
.string_lookup_17/None_Lookup/LookupTableFindV2.string_lookup_17/None_Lookup/LookupTableFindV2:&"
 
_user_specified_name410761:

_output_shapes
: :,(
&
_user_specified_nametable_handle:[ W
#
_output_shapes
:���������
0
_user_specified_namestring_lookup_17_input
�
w
__inference__initializer_410941
unknown
	unknown_0
	unknown_1	
identity��StatefulPartitionedCall�
StatefulPartitionedCallStatefulPartitionedCallunknown	unknown_0	unknown_1*
Tin
2	*
Tout
2*
_collective_manager_ids
 *
_output_shapes
: * 
_read_only_resource_inputs
 *-
config_proto

CPU

GPU 2J 8� *2
f-R+
)__inference_restored_function_body_410931G
ConstConst*
_output_shapes
: *
dtype0*
value	B :L
IdentityIdentityConst:output:0^NoOp*
T0*
_output_shapes
: <
NoOpNoOp^StatefulPartitionedCall*
_output_shapes
 "
identityIdentity:output:0*(
_construction_contextkEagerRuntime*!
_input_shapes
: :i:i22
StatefulPartitionedCallStatefulPartitionedCall: 

_output_shapes
:i: 

_output_shapes
:i:& "
 
_user_specified_name410932"�L
saver_filename:0StatefulPartitionedCall_3:0StatefulPartitionedCall_48"
saved_model_main_op

NoOp*>
__saved_model_init_op%#
__saved_model_init_op

NoOp*�
serving_default�
7
input_1,
serving_default_input_1:0���������>
output_12
StatefulPartitionedCall_1:0���������>
output_22
StatefulPartitionedCall_1:1���������tensorflow/serving/predict:�a
�
	variables
trainable_variables
regularization_losses
	keras_api
__call__
*&call_and_return_all_conditional_losses
_default_save_signature
query_model
	identifiers
	_identifiers


candidates

_candidates
query_with_exclusions

signatures"
_tf_keras_model
5
0
	1

2"
trackable_list_wrapper
'
0"
trackable_list_wrapper
 "
trackable_list_wrapper
�
non_trainable_variables

layers
metrics
layer_regularization_losses
layer_metrics
	variables
trainable_variables
regularization_losses
__call__
_default_save_signature
*&call_and_return_all_conditional_losses
&"call_and_return_conditional_losses"
_generic_user_object
�
trace_0
trace_12�
.__inference_brute_force_2_layer_call_fn_410862
.__inference_brute_force_2_layer_call_fn_410879�
���
FullArgSpec
args�
	jqueries
jk
varargs
 
varkw
 
defaults�

 

kwonlyargs�

jtraining%
kwonlydefaults�

trainingp 
annotations� *
 ztrace_0ztrace_1
�
trace_0
trace_12�
I__inference_brute_force_2_layer_call_and_return_conditional_losses_410825
I__inference_brute_force_2_layer_call_and_return_conditional_losses_410845�
���
FullArgSpec
args�
	jqueries
jk
varargs
 
varkw
 
defaults�

 

kwonlyargs�

jtraining%
kwonlydefaults�

trainingp 
annotations� *
 ztrace_0ztrace_1
�
	capture_1B�
!__inference__wrapped_model_410746input_1"�
���
FullArgSpec
args�

jargs_0
varargs
 
varkw
 
defaults
 

kwonlyargs� 
kwonlydefaults
 
annotations� *
 z	capture_1
�
layer-0
layer_with_weights-0
layer-1
	variables
trainable_variables
regularization_losses
	keras_api
__call__
*&call_and_return_all_conditional_losses
# _self_saveable_object_factories"
_tf_keras_sequential
:�2identifiers
:	�@2
candidates
�2��
���
FullArgSpec)
args!�
	jqueries
j
exclusions
jk
varargs
 
varkw
 
defaults�

 

kwonlyargs� 
kwonlydefaults
 
annotations� *
 
,
!serving_default"
signature_map
):'j@2embedding_17/embeddings
.
	0

1"
trackable_list_wrapper
'
0"
trackable_list_wrapper
 "
trackable_list_wrapper
 "
trackable_list_wrapper
 "
trackable_dict_wrapper
�
	capture_1B�
.__inference_brute_force_2_layer_call_fn_410862input_1"�
���
FullArgSpec
args�
	jqueries
jk
varargs
 
varkw
 
defaults
 

kwonlyargs�

jtraining
kwonlydefaults
 
annotations� *
 z	capture_1
�
	capture_1B�
.__inference_brute_force_2_layer_call_fn_410879input_1"�
���
FullArgSpec
args�
	jqueries
jk
varargs
 
varkw
 
defaults
 

kwonlyargs�

jtraining
kwonlydefaults
 
annotations� *
 z	capture_1
�
	capture_1B�
I__inference_brute_force_2_layer_call_and_return_conditional_losses_410825input_1"�
���
FullArgSpec
args�
	jqueries
jk
varargs
 
varkw
 
defaults
 

kwonlyargs�

jtraining
kwonlydefaults
 
annotations� *
 z	capture_1
�
	capture_1B�
I__inference_brute_force_2_layer_call_and_return_conditional_losses_410845input_1"�
���
FullArgSpec
args�
	jqueries
jk
varargs
 
varkw
 
defaults
 

kwonlyargs�

jtraining
kwonlydefaults
 
annotations� *
 z	capture_1
!J	
Const_2jtf.TrackableConstant
_
"	keras_api
#lookup_table
#$_self_saveable_object_factories"
_tf_keras_layer
�
%	variables
&trainable_variables
'regularization_losses
(	keras_api
)__call__
**&call_and_return_all_conditional_losses

embeddings
#+_self_saveable_object_factories"
_tf_keras_layer
'
0"
trackable_list_wrapper
'
0"
trackable_list_wrapper
 "
trackable_list_wrapper
�
,non_trainable_variables

-layers
.metrics
/layer_regularization_losses
0layer_metrics
	variables
trainable_variables
regularization_losses
__call__
*&call_and_return_all_conditional_losses
&"call_and_return_conditional_losses"
_generic_user_object
�
1trace_0
2trace_12�
.__inference_sequential_25_layer_call_fn_410787
.__inference_sequential_25_layer_call_fn_410798�
���
FullArgSpec)
args!�
jinputs

jtraining
jmask
varargs
 
varkw
 
defaults�
p 

 

kwonlyargs� 
kwonlydefaults
 
annotations� *
 z1trace_0z2trace_1
�
3trace_0
4trace_12�
I__inference_sequential_25_layer_call_and_return_conditional_losses_410765
I__inference_sequential_25_layer_call_and_return_conditional_losses_410776�
���
FullArgSpec)
args!�
jinputs

jtraining
jmask
varargs
 
varkw
 
defaults�
p 

 

kwonlyargs� 
kwonlydefaults
 
annotations� *
 z3trace_0z4trace_1
 "
trackable_dict_wrapper
�
	capture_1B�
$__inference_signature_wrapper_410897input_1"�
���
FullArgSpec
args� 
varargs
 
varkw
 
defaults
 

kwonlyargs�
	jinput_1
kwonlydefaults
 
annotations� *
 z	capture_1
"
_generic_user_object
f
5_initializer
6_create_resource
7_initialize
8_destroy_resourceR jtf.StaticHashTable
 "
trackable_dict_wrapper
'
0"
trackable_list_wrapper
'
0"
trackable_list_wrapper
 "
trackable_list_wrapper
�
9non_trainable_variables

:layers
;metrics
<layer_regularization_losses
=layer_metrics
%	variables
&trainable_variables
'regularization_losses
)__call__
**&call_and_return_all_conditional_losses
&*"call_and_return_conditional_losses"
_generic_user_object
�
>trace_02�
-__inference_embedding_17_layer_call_fn_410904�
���
FullArgSpec
args�

jinputs
varargs
 
varkw
 
defaults
 

kwonlyargs� 
kwonlydefaults
 
annotations� *
 z>trace_0
�
?trace_02�
H__inference_embedding_17_layer_call_and_return_conditional_losses_410912�
���
FullArgSpec
args�

jinputs
varargs
 
varkw
 
defaults
 

kwonlyargs� 
kwonlydefaults
 
annotations� *
 z?trace_0
 "
trackable_dict_wrapper
 "
trackable_list_wrapper
.
0
1"
trackable_list_wrapper
 "
trackable_list_wrapper
 "
trackable_list_wrapper
 "
trackable_dict_wrapper
�
	capture_1B�
.__inference_sequential_25_layer_call_fn_410787string_lookup_17_input"�
���
FullArgSpec)
args!�
jinputs

jtraining
jmask
varargs
 
varkw
 
defaults
 

kwonlyargs� 
kwonlydefaults
 
annotations� *
 z	capture_1
�
	capture_1B�
.__inference_sequential_25_layer_call_fn_410798string_lookup_17_input"�
���
FullArgSpec)
args!�
jinputs

jtraining
jmask
varargs
 
varkw
 
defaults
 

kwonlyargs� 
kwonlydefaults
 
annotations� *
 z	capture_1
�
	capture_1B�
I__inference_sequential_25_layer_call_and_return_conditional_losses_410765string_lookup_17_input"�
���
FullArgSpec)
args!�
jinputs

jtraining
jmask
varargs
 
varkw
 
defaults
 

kwonlyargs� 
kwonlydefaults
 
annotations� *
 z	capture_1
�
	capture_1B�
I__inference_sequential_25_layer_call_and_return_conditional_losses_410776string_lookup_17_input"�
���
FullArgSpec)
args!�
jinputs

jtraining
jmask
varargs
 
varkw
 
defaults
 

kwonlyargs� 
kwonlydefaults
 
annotations� *
 z	capture_1
"
_generic_user_object
�
@trace_02�
__inference__creator_410920�
���
FullArgSpec
args� 
varargs
 
varkw
 
defaults
 

kwonlyargs� 
kwonlydefaults
 
annotations� *� z@trace_0
�
Atrace_02�
__inference__initializer_410941�
���
FullArgSpec
args� 
varargs
 
varkw
 
defaults
 

kwonlyargs� 
kwonlydefaults
 
annotations� *� zAtrace_0
�
Btrace_02�
__inference__destroyer_410950�
���
FullArgSpec
args� 
varargs
 
varkw
 
defaults
 

kwonlyargs� 
kwonlydefaults
 
annotations� *� zBtrace_0
 "
trackable_list_wrapper
 "
trackable_list_wrapper
 "
trackable_list_wrapper
 "
trackable_list_wrapper
 "
trackable_dict_wrapper
�B�
-__inference_embedding_17_layer_call_fn_410904inputs"�
���
FullArgSpec
args�

jinputs
varargs
 
varkw
 
defaults
 

kwonlyargs� 
kwonlydefaults
 
annotations� *
 
�B�
H__inference_embedding_17_layer_call_and_return_conditional_losses_410912inputs"�
���
FullArgSpec
args�

jinputs
varargs
 
varkw
 
defaults
 

kwonlyargs� 
kwonlydefaults
 
annotations� *
 
�B�
__inference__creator_410920"�
���
FullArgSpec
args� 
varargs
 
varkw
 
defaults
 

kwonlyargs� 
kwonlydefaults
 
annotations� *� 
�
C	capture_1
D	capture_2B�
__inference__initializer_410941"�
���
FullArgSpec
args� 
varargs
 
varkw
 
defaults
 

kwonlyargs� 
kwonlydefaults
 
annotations� *� zC	capture_1zD	capture_2
�B�
__inference__destroyer_410950"�
���
FullArgSpec
args� 
varargs
 
varkw
 
defaults
 

kwonlyargs� 
kwonlydefaults
 
annotations� *� 
!J	
Const_1jtf.TrackableConstant
J
Constjtf.TrackableConstant@
__inference__creator_410920!�

� 
� "�
unknown B
__inference__destroyer_410950!�

� 
� "�
unknown I
__inference__initializer_410941&#CD�

� 
� "�
unknown �
!__inference__wrapped_model_410746�#
	,�)
"�
�
input_1���������
� "c�`
.
output_1"�
output_1���������
.
output_2"�
output_2����������
I__inference_brute_force_2_layer_call_and_return_conditional_losses_410825�#
	@�=
&�#
�
input_1���������

 
�

trainingp"Y�V
O�L
$�!

tensor_0_0���������
$�!

tensor_0_1���������
� �
I__inference_brute_force_2_layer_call_and_return_conditional_losses_410845�#
	@�=
&�#
�
input_1���������

 
�

trainingp "Y�V
O�L
$�!

tensor_0_0���������
$�!

tensor_0_1���������
� �
.__inference_brute_force_2_layer_call_fn_410862�#
	@�=
&�#
�
input_1���������

 
�

trainingp"K�H
"�
tensor_0���������
"�
tensor_1����������
.__inference_brute_force_2_layer_call_fn_410879�#
	@�=
&�#
�
input_1���������

 
�

trainingp "K�H
"�
tensor_0���������
"�
tensor_1����������
H__inference_embedding_17_layer_call_and_return_conditional_losses_410912^+�(
!�
�
inputs���������	
� ",�)
"�
tensor_0���������@
� �
-__inference_embedding_17_layer_call_fn_410904S+�(
!�
�
inputs���������	
� "!�
unknown���������@�
I__inference_sequential_25_layer_call_and_return_conditional_losses_410765x#C�@
9�6
,�)
string_lookup_17_input���������
p

 
� ",�)
"�
tensor_0���������@
� �
I__inference_sequential_25_layer_call_and_return_conditional_losses_410776x#C�@
9�6
,�)
string_lookup_17_input���������
p 

 
� ",�)
"�
tensor_0���������@
� �
.__inference_sequential_25_layer_call_fn_410787m#C�@
9�6
,�)
string_lookup_17_input���������
p

 
� "!�
unknown���������@�
.__inference_sequential_25_layer_call_fn_410798m#C�@
9�6
,�)
string_lookup_17_input���������
p 

 
� "!�
unknown���������@�
$__inference_signature_wrapper_410897�#
	7�4
� 
-�*
(
input_1�
input_1���������"c�`
.
output_1"�
output_1���������
.
output_2"�
output_2���������