<html>
<head>
<title>Cadastro de cidade - üDOCTOR</title>
<meta name="layout" content="principal"/>
<parameter name="searchbarAtivo" value="X" /> 
<r:require module="core"/>
</head>
<body>
	<div id="breadcrumb">
		<div class="container">
			<h2>Admin</h2>
			<div id="breadcrumb-info">Inserir cidade</div>
		</div>
	</div>

	<div class="container content">
		<form action="cadastroCidade" method="POST"
				name="cadastroCidade" id="left-fields" class="content-left">
			<div id="admin-listing" class="content-middle">
				<div class="form-actions">
					<div class="pull-right">
						<button type="submit" class="btn btn-success">Salvar</button>
						<button type="button" class="btn">Cancelar</button>
					</div>
				</div>
			
				<h1>Cadastro de cidade</h1>
				<br>

				<label class="form-field">
				Cidade:<br>
				<input type="text">
				</label>
			
				<label class="form-field">
					Estado:<br>
					<select data-placeholder="Selecione..." class="chzn-select" tabindex="1">
						<option value=""></option>
						<option value="">---</option>
					</select>
				</label>
			</div>
		</form>
	</div>
</body>
</html>