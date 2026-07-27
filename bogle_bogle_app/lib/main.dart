import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

void main() {
  runApp(const BogleBogleApp());
}

// 📦 재료 데이터 모델
class IngredientItem {
  final int? id; // 백엔드 ID (기본 백엔드 매핑용 또는 자동할당용)
  final String name; // 재료 이름
  int count; // 수량
  bool isSelected; // 주방에서 선택 여부

  IngredientItem({
    this.id,
    required this.name,
    required this.count,
    this.isSelected = false,
  });
}

class BogleBogleApp extends StatelessWidget {
  const BogleBogleApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '보글보글 재료마을',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.orange,
        useMaterial3: true,
      ),
      home: const MainPage(),
    );
  }
}

// 🏠 1. 메인 화면
class MainPage extends StatefulWidget {
  const MainPage({super.key});

  @override
  State<MainPage> createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> {
  // 🧊 냉장고에 보관 중인 재료 리스트 (기본 예시 데이터)
  final List<IngredientItem> _refrigeratorIngredients = [
    IngredientItem(id: 1, name: '김치', count: 1),
    IngredientItem(id: 2, name: '돼지고기', count: 2),
    IngredientItem(id: 3, name: '두부', count: 1),
    IngredientItem(id: 4, name: '된장', count: 1),
    IngredientItem(id: 5, name: '감자', count: 3),
    IngredientItem(id: 6, name: '양파', count: 2),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text(
          '🍲 보글보글 재료마을 🌿',
          style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
        ),
        backgroundColor: Colors.orange,
        centerTitle: true,
      ),
      body: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            // 🧊 냉장고 버튼 (재료 등록/관리)
            SizedBox(
              width: double.infinity,
              height: 120,
              child: ElevatedButton.icon(
                onPressed: () async {
                  await Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => RefrigeratorPage(ingredients: _refrigeratorIngredients),
                    ),
                  );
                  setState(() {}); // 냉장고 수정 완료 후 메인 UI 갱신
                },
                icon: const Icon(Icons.kitchen, color: Colors.white, size: 40),
                label: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const Text('🧊 냉장고 (재료 관리)', style: TextStyle(fontSize: 22, color: Colors.white, fontWeight: FontWeight.bold)),
                    Text('보관 중인 재료: ${_refrigeratorIngredients.length}종류', style: const TextStyle(fontSize: 14, color: Colors.white70)),
                  ],
                ),
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.orange,
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                ),
              ),
            ),

            const SizedBox(height: 30),

            // 🍳 주방 버튼 (냉장고 재료 중 선택하여 레시피 검색)
            SizedBox(
              width: double.infinity,
              height: 120,
              child: ElevatedButton.icon(
                onPressed: () {
                  if (_refrigeratorIngredients.isEmpty) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text('냉장고에 재료가 없습니다. 먼저 냉장고에서 재료를 추가해주세요!'), backgroundColor: Colors.red),
                    );
                    return;
                  }

                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => KitchenPage(refrigeratorIngredients: _refrigeratorIngredients),
                    ),
                  );
                },
                icon: const Icon(Icons.restaurant, color: Colors.white, size: 40),
                label: const Text('🍳 주방 (레시피 검색)', style: TextStyle(fontSize: 22, color: Colors.white, fontWeight: FontWeight.bold)),
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.deepOrange,
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

// 🧊 2. 냉장고 화면 (재료 이름, 수량 직접 추가 & 삭제)
class RefrigeratorPage extends StatefulWidget {
  final List<IngredientItem> ingredients;

  const RefrigeratorPage({super.key, required this.ingredients});

  @override
  State<RefrigeratorPage> createState() => _RefrigeratorPageState();
}

class _RefrigeratorPageState extends State<RefrigeratorPage> {
  // 재료 추가 팝업(Dialog) 띄우기
  void _showAddIngredientDialog() {
    final nameController = TextEditingController();
    final countController = TextEditingController(text: '1');

    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('🧊 새 재료 추가'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(
                controller: nameController,
                decoration: const InputDecoration(
                  labelText: '재료 이름',
                  hintText: '예: 당근, 대파, 쇠고기',
                ),
              ),
              const SizedBox(height: 10),
              TextField(
                controller: countController,
                keyboardType: TextInputType.number,
                decoration: const InputDecoration(
                  labelText: '수량',
                  hintText: '숫자 입력',
                ),
              ),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('취소'),
            ),
            ElevatedButton(
              onPressed: () {
                final name = nameController.text.trim();
                final count = int.tryParse(countController.text.trim()) ?? 1;

                if (name.isNotEmpty) {
                  setState(() {
                    widget.ingredients.add(
                      IngredientItem(name: name, count: count),
                    );
                  });
                  Navigator.pop(context);
                }
              },
              style: ElevatedButton.styleFrom(backgroundColor: Colors.orange),
              child: const Text('추가', style: TextStyle(color: Colors.white)),
            ),
          ],
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('🧊 나의 냉장고', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
        backgroundColor: Colors.orange,
        iconTheme: const IconThemeData(color: Colors.white),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            Expanded(
              child: widget.ingredients.isEmpty
                  ? const Center(child: Text('냉장고가 비어있습니다.\n아래 + 버튼을 눌러 재료를 추가해주세요!'))
                  : ListView.builder(
                itemCount: widget.ingredients.length,
                itemBuilder: (context, index) {
                  final item = widget.ingredients[index];
                  return Card(
                    margin: const EdgeInsets.symmetric(vertical: 6),
                    child: ListTile(
                      leading: const CircleAvatar(
                        backgroundColor: Colors.orange,
                        child: Icon(Icons.kitchen, color: Colors.white),
                      ),
                      title: Text(item.name, style: const TextStyle(fontWeight: FontWeight.bold)),
                      subtitle: Text('수량: ${item.count}개'),
                      trailing: IconButton(
                        icon: const Icon(Icons.delete_outline, color: Colors.red),
                        onPressed: () {
                          setState(() {
                            widget.ingredients.removeAt(index);
                          });
                        },
                      ),
                    ),
                  );
                },
              ),
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: _showAddIngredientDialog,
        backgroundColor: Colors.orange,
        icon: const Icon(Icons.add, color: Colors.white),
        label: const Text('재료 추가', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
      ),
    );
  }
}

// 🍳 3. 주방 화면 (냉장고에 담긴 재료 중 선택 후 레시피 검색)
class KitchenPage extends StatefulWidget {
  final List<IngredientItem> refrigeratorIngredients;

  const KitchenPage({super.key, required this.refrigeratorIngredients});

  @override
  State<KitchenPage> createState() => _KitchenPageState();
}

class _KitchenPageState extends State<KitchenPage> {
  List<dynamic> _matchedRecipes = [];
  bool _isLoading = false;

  // 선택된 재료로 백엔드 레시피 검색 API 호출
  Future<void> _searchRecipesFromBackend() async {
    // 선택된 재료들만 필터링
    final selectedItems = widget.refrigeratorIngredients.where((item) => item.isSelected).toList();

    if (selectedItems.isEmpty) {
      _showSnackBar('요리에 사용할 재료를 1개 이상 선택해 주세요!');
      return;
    }

    setState(() {
      _isLoading = true;
    });

    // ID가 있는 경우 ID를 사용하고, 없는 경우 이름(or 기본값)을 파라미터로 넘깁니다.
    final String idsParam = selectedItems
        .map((e) => e.id ?? e.name)
        .join(',');

    final Uri url = Uri.parse('http://localhost:8080/api/recipes/match?ingredientIds=$idsParam');

    try {
      final response = await http.get(url);

      if (response.statusCode == 200) {
        final List<dynamic> data = json.decode(utf8.decode(response.bodyBytes));
        setState(() {
          _matchedRecipes = data;
        });
      } else {
        _showSnackBar('서버 응답 에러: ${response.statusCode}');
      }
    } catch (e) {
      print('백엔드 연결 실패: $e');
      _showSnackBar('백엔드 서버와 연결할 수 없습니다. (localhost:8080 확인)');
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  void _showSnackBar(String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(message), backgroundColor: Colors.red),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('🍳 보글보글 주방', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
        backgroundColor: Colors.deepOrange,
        iconTheme: const IconThemeData(color: Colors.white),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              '📦 냉장고 재료 중 사용할 재료를 선택하세요',
              style: TextStyle(fontSize: 17, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 10),

            // 냉장고 재료 선택 그리드/칩 목록
            SizedBox(
              height: 120,
              child: SingleChildScrollView(
                child: Wrap(
                  spacing: 8.0,
                  runSpacing: 4.0,
                  children: widget.refrigeratorIngredients.map((item) {
                    return FilterChip(
                      label: Text('${item.name} (${item.count}개)'),
                      selected: item.isSelected,
                      selectedColor: Colors.deepOrange.shade100,
                      checkmarkColor: Colors.deepOrange,
                      onSelected: (bool selected) {
                        setState(() {
                          item.isSelected = selected;
                        });
                      },
                    );
                  }).toList(),
                ),
              ),
            ),

            const SizedBox(height: 10),

            // 레시피 검색 실행 버튼
            SizedBox(
              width: double.infinity,
              height: 48,
              child: ElevatedButton.icon(
                onPressed: _searchRecipesFromBackend,
                icon: const Icon(Icons.search, color: Colors.white),
                label: const Text('선택한 재료로 레시피 검색', style: TextStyle(fontSize: 16, color: Colors.white, fontWeight: FontWeight.bold)),
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.deepOrange,
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                ),
              ),
            ),

            const SizedBox(height: 15),
            const Divider(thickness: 1.5),
            const SizedBox(height: 10),

            const Text(
              '🍳 추천 레시피 결과',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 10),

            // 백엔드 응답 결과 리스트
            Expanded(
              child: _isLoading
                  ? const Center(child: CircularProgressIndicator(color: Colors.deepOrange))
                  : _matchedRecipes.isEmpty
                  ? const Center(child: Text('위에서 재료를 선택하고 [레시피 검색] 버튼을 눌러보세요!'))
                  : ListView.builder(
                itemCount: _matchedRecipes.length,
                itemBuilder: (context, index) {
                  final recipe = _matchedRecipes[index];
                  return Card(
                    margin: const EdgeInsets.symmetric(vertical: 6),
                    elevation: 2,
                    child: ListTile(
                      leading: CircleAvatar(
                        backgroundColor: Colors.deepOrange.shade400,
                        child: const Icon(Icons.restaurant, color: Colors.white, size: 20),
                      ),
                      title: Text(
                        recipe['title'] ?? recipe['name'] ?? '제목 없음',
                        style: const TextStyle(fontWeight: FontWeight.bold),
                      ),
                      subtitle: Text(recipe['description'] ?? recipe['ingredients'] ?? ''),
                      trailing: const Icon(Icons.arrow_forward_ios, size: 16),
                      onTap: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => RecipeDetailPage(recipe: recipe),
                          ),
                        );
                      },
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

// 📖 4. 레시피 상세 화면
class RecipeDetailPage extends StatelessWidget {
  final dynamic recipe;

  const RecipeDetailPage({super.key, required this.recipe});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          recipe['title'] ?? recipe['name'] ?? '레시피 상세',
          style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
        ),
        backgroundColor: Colors.deepOrange,
        iconTheme: const IconThemeData(color: Colors.white),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Center(
              child: Container(
                width: 120,
                height: 120,
                decoration: BoxDecoration(
                  color: Colors.orange.shade100,
                  shape: BoxShape.circle,
                ),
                child: const Icon(Icons.restaurant_menu, size: 60, color: Colors.deepOrange),
              ),
            ),
            const SizedBox(height: 20),
            Text(
              recipe['title'] ?? recipe['name'] ?? '제목 없음',
              style: const TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 10),
            Text(
              recipe['description'] ?? recipe['ingredients'] ?? '상세 설명이 없습니다.',
              style: TextStyle(fontSize: 16, color: Colors.grey.shade700, height: 1.5),
            ),
            const SizedBox(height: 30),
            const Divider(thickness: 1.5),
            const SizedBox(height: 20),
            const Text(
              '👨‍🍳 조리 방법',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 12),
            Container(
              padding: const EdgeInsets.all(16),
              width: double.infinity,
              decoration: BoxDecoration(
                color: Colors.grey.shade100,
                borderRadius: BorderRadius.circular(12),
              ),
              child: Text(
                recipe['instructions'] ?? recipe['content'] ?? '1. 재료를 먹기 좋게 손질합니다.\n2. 냄비에 물과 재료를 넣고 끓입니다.\n3. 간을 맞추고 맛있게 드세요!',
                style: const TextStyle(fontSize: 15, height: 1.6),
              ),
            ),
          ],
         ),
      ),
    );
  }
}