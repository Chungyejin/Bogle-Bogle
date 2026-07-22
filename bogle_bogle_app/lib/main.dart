import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

void main() {
  runApp(const BogleBogleApp());
}

class BogleBogleApp extends StatelessWidget {
  const BogleBogleApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '보글보글 레시피',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.orange,
        useMaterial3: true,
      ),
      home: const RecipeMatchPage(),
    );
  }
}

class RecipeMatchPage extends StatefulWidget {
  const RecipeMatchPage({super.key});

  @override
  State<RecipeMatchPage> createState() => _RecipeMatchPageState();
}

class _RecipeMatchPageState extends State<RecipeMatchPage> {
  // 1. 재료 목록과 백엔드 DB의 ingredientId 매핑
  final Map<String, Map<String, dynamic>> _ingredients = {
    '김치': {'id': 1, 'selected': false},
    '돼지고기': {'id': 2, 'selected': false},
    '두부': {'id': 3, 'selected': false},
    '된장': {'id': 4, 'selected': false},
    '감자': {'id': 5, 'selected': false},
    '양파': {'id': 6, 'selected': false},
    '대파': {'id': 7, 'selected': false},
    '계란': {'id': 8, 'selected': false},
  };

  List<dynamic> _matchedRecipes = [];
  bool _isLoading = false;

  // 🌐 2. 백엔드 API 연동 함수 (GET /api/recipes/match?ingredientIds=1,2...)
  Future<void> _searchRecipesFromBackend() async {
    // 선택된 재료들의 ID 추출
    List<int> selectedIds = _ingredients.values
        .where((item) => item['selected'] == true)
        .map<int>((item) => item['id'] as int)
        .toList();

    if (selectedIds.isEmpty) {
      _showErrorSnackBar('재료를 1개 이상 선택해 주세요!');
      return;
    }

    setState(() {
      _isLoading = true;
    });

    final String idsParam = selectedIds.join(',');
    final Uri url = Uri.parse('http://localhost:8080/api/recipes/match?ingredientIds=$idsParam');

    try {
      final response = await http.get(url);

      if (response.statusCode == 200) {
        final List<dynamic> data = json.decode(utf8.decode(response.bodyBytes));
        setState(() {
          _matchedRecipes = data;
        });
      } else {
        _showErrorSnackBar('서버 응답 에러: ${response.statusCode}');
      }
    } catch (e) {
      print('백엔드 연결 실패: $e');
      _showErrorSnackBar('백엔드 서버와 연결할 수 없습니다. (localhost:8080 확인)');
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  void _showErrorSnackBar(String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(message), backgroundColor: Colors.red),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('🍲 보글보글 레시피 매칭', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
        backgroundColor: Colors.orange,
        centerTitle: true,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              '📦 냉장고에 어떤 재료가 있나요?',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 10),

            // 재료 선택 그리드
            Expanded(
              flex: 2,
              child: GridView.builder(
                gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                  crossAxisCount: 3,
                  childAspectRatio: 2.5,
                  crossAxisSpacing: 8,
                  mainAxisSpacing: 8,
                ),
                itemCount: _ingredients.length,
                itemBuilder: (context, index) {
                  String name = _ingredients.keys.elementAt(index);
                  bool isSelected = _ingredients[name]!['selected'];

                  return FilterChip(
                    label: Center(child: Text(name)),
                    selected: isSelected,
                    selectedColor: Colors.orange.shade100,
                    checkmarkColor: Colors.orange,
                    onSelected: (bool value) {
                      setState(() {
                        _ingredients[name]!['selected'] = value;
                      });
                    },
                  );
                },
              ),
            ),

            // 매칭 버튼
            SizedBox(
              width: double.infinity,
              height: 50,
              child: ElevatedButton.icon(
                onPressed: _searchRecipesFromBackend,
                icon: const Icon(Icons.search, color: Colors.white),
                label: const Text('백엔드에서 레시피 검색', style: TextStyle(fontSize: 16, color: Colors.white, fontWeight: FontWeight.bold)),
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.orange,
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                ),
              ),
            ),
            const SizedBox(height: 20),
            const Divider(thickness: 1.5),
            const SizedBox(height: 10),

            const Text(
              '🍳 추천 레시피 결과',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 10),

            // 결과 리스트뷰
            Expanded(
              flex: 3,
              child: _isLoading
                  ? const Center(child: CircularProgressIndicator(color: Colors.orange))
                  : _matchedRecipes.isEmpty
                  ? const Center(child: Text('재료를 선택하고 레시피를 검색해보세요!'))
                  : ListView.builder(
                itemCount: _matchedRecipes.length,
                itemBuilder: (context, index) {
                  final recipe = _matchedRecipes[index];
                  return Card(
                    margin: const EdgeInsets.symmetric(vertical: 6),
                    elevation: 2,
                    child: ListTile(
                      leading: CircleAvatar(
                        backgroundColor: Colors.orange.shade400,
                        child: const Icon(Icons.restaurant, color: Colors.white, size: 20),
                      ),
                      // RecipeResponse 필드명에 맞춰 필드 조율 (title / name 등)
                      title: Text(recipe['title'] ?? recipe['name'] ?? '제목 없음', style: const TextStyle(fontWeight: FontWeight.bold)),
                      subtitle: Text(recipe['description'] ?? recipe['ingredients'] ?? ''),
                      trailing: const Icon(Icons.arrow_forward_ios, size: 16),
                    ),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}